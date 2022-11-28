package io.smanicome.racemanager.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smanicome.racemanager.core.Race;
import io.smanicome.racemanager.core.Runner;
import io.smanicome.racemanager.service.RaceManagerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RaceControllerShould {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RaceManagerService raceManagerService;

    @DisplayName("create given race")
    @Test
    void createTodo() throws Exception {
        final var raceId = UUID.randomUUID();
        final var raceDate = LocalDate.now();
        final var raceNumber = 1;

        final var runnerId1 = UUID.randomUUID();
        final var runnerId2 = UUID.randomUUID();
        final var runnerId3 = UUID.randomUUID();
        String runnerTitle1 = "runner 1";
        String runnerTitle2 = "runner 2";
        String runnerTitle3 = "runner 3";
        int runnerNumber1 = 1;
        int runnerNumber2 = 2;
        int runnerNumber3 = 3;

        final var runners = List.of(
                new Runner(runnerId1, runnerTitle1, runnerNumber1),
                new Runner(runnerId2, runnerTitle2, runnerNumber2),
                new Runner(runnerId3, runnerTitle3, runnerNumber3)
        );

        final var race = new Race(raceId, raceDate, raceNumber, runners);

        final var expectedRunnerResponses = List.of(
                new RunnerResponse(runnerId1, runnerTitle1, runnerNumber1),
                new RunnerResponse(runnerId2, runnerTitle2, runnerNumber2),
                new RunnerResponse(runnerId3, runnerTitle3, runnerNumber3)
        );
        final var expectedResponse = new RaceResponse(raceId, raceDate, raceNumber, expectedRunnerResponses);

        when(raceManagerService.createRace(any())).thenReturn(race);

        final var runnerRequests = List.of(
                new RunnerCreationRequest(runnerTitle1, runnerNumber1),
                new RunnerCreationRequest(runnerTitle2, runnerNumber2),
                new RunnerCreationRequest(runnerTitle3, runnerNumber3)
        );
        final var raceRequest = new RaceCreationRequest(raceDate, raceNumber, runnerRequests);

        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(raceRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(expectedResponse), true));

        final var runnersToSave = List.of(
                new Runner(null, runnerTitle1, runnerNumber1),
                new Runner(null, runnerTitle2, runnerNumber2),
                new Runner(null, runnerTitle3, runnerNumber3)
        );

        final var raceToSave = new Race(null, raceDate, raceNumber, runnersToSave);

        verify(raceManagerService).createRace(raceToSave);
        verifyNoMoreInteractions(raceManagerService);
    }

    @DisplayName("return bad request if date is not specified")
    @Test
    void returnBadRequestIfDateIsNotSpecified() throws Exception {
        final var runnerRequests = List.of(
            new RunnerCreationRequest("runner 1", 1),
            new RunnerCreationRequest("runner 2", 2),
            new RunnerCreationRequest("runner 3", 3)
        );
        final var raceRequest = new RaceCreationRequest(null, 1, runnerRequests);

        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(raceRequest))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(raceManagerService);
    }

    @DisplayName("return bad request if an invalid race number is sent")
    @ParameterizedTest(name = "with race number as {0}")
    @ValueSource(ints = {0, -1})
    void returnBadRequestIfAnInvalidRaceNumberIsSent(int raceNumber) throws Exception {
        final var runnerRequests = List.of(
                new RunnerCreationRequest("runner 1", 1),
                new RunnerCreationRequest("runner 2", 2),
                new RunnerCreationRequest("runner 3", 3)
        );
        final var raceRequest = new RaceCreationRequest(LocalDate.now(), raceNumber, runnerRequests);

        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(raceRequest))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(raceManagerService);
    }

    @DisplayName("return bad request if no runners are sent")
    @Test
    void returnBadRequestIfNoRunnersAreSent() throws Exception {
        final var raceRequest = new RaceCreationRequest(LocalDate.now(), 1, null);

        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(raceRequest))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(raceManagerService);
    }

    @DisplayName("return bad request if insufficient runners are sent")
    @Test
    void returnBadRequestIfInsufficientRunnersAreSent() throws Exception {
        final var runnerRequests = List.of(
                new RunnerCreationRequest("runner 1", 1),
                new RunnerCreationRequest("runner 2", 2)
        );
        final var raceRequest = new RaceCreationRequest(LocalDate.now(), 1, runnerRequests);

        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(raceRequest))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(raceManagerService);
    }

    @DisplayName("return bad request if a runner has an invalid name")
    @ParameterizedTest(name = "with name as \"{0}\"")
    @CsvSource(value = {
        "''",
        "'   '",
        "'\t'",
        "null"
    }, nullValues = "null")
    void returnBadRequestIfARunnerHasAnInvalidName(String runnerName) throws Exception {
        final var runnerRequests = List.of(
                new RunnerCreationRequest("runner 1", 1),
                new RunnerCreationRequest("runner 2", 2),
                new RunnerCreationRequest(runnerName, 3)
        );
        final var raceRequest = new RaceCreationRequest(LocalDate.now(), 1, runnerRequests);

        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(raceRequest))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(raceManagerService);
    }

    @DisplayName("return bad request if an invalid runner number is sent")
    @ParameterizedTest(name = "with runner number as {0}")
    @ValueSource(ints = {0, -1})
    void returnBadRequestIfARunnerHasAnInvalidRunnerNumber(int raceNumber) throws Exception {
        final var runnerRequests = List.of(
                new RunnerCreationRequest("runner 1", 1),
                new RunnerCreationRequest("runner 2", 2),
                new RunnerCreationRequest("runner 3", raceNumber)
        );
        final var raceRequest = new RaceCreationRequest(LocalDate.now(), 1, runnerRequests);

        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(raceRequest))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(raceManagerService);
    }
}