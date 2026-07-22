package com.greencharge.sessionservice.controller;

import com.greencharge.sessionservice.dto.SessionResponseDTO;
import com.greencharge.sessionservice.dto.StartSessionRequestDTO;
import com.greencharge.sessionservice.dto.StopSessionRequestDTO;
import com.greencharge.sessionservice.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@Slf4j
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/start")
    public ResponseEntity<SessionResponseDTO> startSession(
            @Valid @RequestBody StartSessionRequestDTO requestDTO) {
        log.info("I am inside the method: startSession");
        SessionResponseDTO response = sessionService.startSession(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/stop")
    public ResponseEntity<SessionResponseDTO> stopSession(
            @PathVariable Long id, @Valid @RequestBody StopSessionRequestDTO requestDTO) {
        log.info("I am inside the method: stopSession");
        return ResponseEntity.ok(sessionService.stopSession(id, requestDTO));
    }

    @PutMapping("/{id}/terminate")
    public ResponseEntity<SessionResponseDTO> terminateSession(@PathVariable Long id) {
        log.info("I am inside the method: terminateSession");
        return ResponseEntity.ok(sessionService.terminateSession(id));
    }

    @GetMapping
    public ResponseEntity<List<SessionResponseDTO>> getAllSessions(
            @RequestParam(required = false) String userId) {
        log.info("I am inside the method: getAllSessions");
        List<SessionResponseDTO> sessions = (userId != null && !userId.isBlank())
                ? sessionService.getSessionsByUser(userId)
                : sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionResponseDTO> getSessionById(@PathVariable Long id) {
        log.info("I am inside the method: getSessionById");
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }
}
