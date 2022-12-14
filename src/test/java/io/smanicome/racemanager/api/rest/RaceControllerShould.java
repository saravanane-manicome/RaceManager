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
        final var raceName = "race";
        final var raceDate = LocalDate.now();
        final var raceNumber = 1;

        final var runnerId1 = UUID.randomUUID();
        final var runnerId2 = UUID.randomUUID();
        final var runnerId3 = UUID.randomUUID();
        final var runnerName1 = "runner 1";
        final var runnerName2 = "runner 2";
        final var runnerName3 = "runner 3";
        final var runnerNumber1 = 1;
        final var runnerNumber2 = 2;
        final var runnerNumber3 = 3;

        final var runners = List.of(
                new Runner(runnerId1, runnerName1, runnerNumber1),
                new Runner(runnerId2, runnerName2, runnerNumber2),
                new Runner(runnerId3, runnerName3, runnerNumber3)
        );

        final var race = new Race(raceId, raceName, raceDate, raceNumber, runners);

        final var expectedRunnerResponses = List.of(
                new RunnerResponse(runnerId1, runnerName1, runnerNumber1),
                new RunnerResponse(runnerId2, runnerName2, runnerNumber2),
                new RunnerResponse(runnerId3, runnerName3, runnerNumber3)
        );
        final var expectedResponse = new RaceResponse(raceId, raceName, raceDate, raceNumber, expectedRunnerResponses);

        final var runnersToSave = List.of(
                new Runner(null, runnerName1, runnerNumber1),
                new Runner(null, runnerName2, runnerNumber2),
                new Runner(null, runnerName3, runnerNumber3)
        );

        final var runnerRequests = List.of(
                new RunnerCreationRequest(runnerName1, runnerNumber1),
                new RunnerCreationRequest(runnerName2, runnerNumber2),
                new RunnerCreationRequest(runnerName3, runnerNumber3)
        );
        final var raceRequest = new RaceCreationRequest(raceName, raceDate, raceNumber, runnerRequests);

        final var raceToSave = new Race(null, raceName, raceDate, raceNumber, runnersToSave);

        when(raceManagerService.createRace(any())).thenReturn(race);

        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(raceRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(expectedResponse), true));


        verify(raceManagerService).createRace(raceToSave);
        verifyNoMoreInteractions(raceManagerService);
    }

    @DisplayName("return bad request if a runner has an invalid name")
    @ParameterizedTest(name = "with name as \"{0}\"")
    @CsvSource(value = {
            "''",
            "'   '",
            "'\t'",
            "null"
    }, nullValues = "null")
    void returnBadRequestIfRaceHasAnInvalidName(String raceName) throws Exception {
        final var runnerRequests = List.of(
                new RunnerCreationRequest("runner 1", 1),
                new RunnerCreationRequest("runner 2", 2),
                new RunnerCreationRequest("runner 3", 3)
        );
        final var raceRequest = new RaceCreationRequest(raceName, LocalDate.now(), 1, runnerRequests);

        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(raceRequest))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(raceManagerService);
    }

    @DisplayName("return bad request if date is not specified")
    @Test
    void returnBadRequestIfDateIsNotSpecified() throws Exception {
        final var runnerRequests = List.of(
            new RunnerCreationRequest("runner 1", 1),
            new RunnerCreationRequest("runner 2", 2),
            new RunnerCreationRequest("runner 3", 3)
        );
        final var raceRequest = new RaceCreationRequest("race", null, 1, runnerRequests);

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
        final var raceRequest = new RaceCreationRequest("race", LocalDate.now(), raceNumber, runnerRequests);

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
        final var raceRequest = new RaceCreationRequest("race", LocalDate.now(), 1, null);

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
        final var raceRequest = new RaceCreationRequest("race", LocalDate.now(), 1, runnerRequests);

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
        final var raceRequest = new RaceCreationRequest("race", LocalDate.now(), 1, runnerRequests);

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
        final var raceRequest = new RaceCreationRequest("race", LocalDate.now(), 1, runnerRequests);

        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(raceRequest))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(raceManagerService);
    }
}