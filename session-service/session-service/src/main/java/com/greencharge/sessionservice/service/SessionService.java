package com.greencharge.sessionservice.service;

import com.greencharge.sessionservice.dto.SessionResponseDTO;
import com.greencharge.sessionservice.dto.StartSessionRequestDTO;
import com.greencharge.sessionservice.dto.StopSessionRequestDTO;

import java.util.List;

public interface SessionService {

    SessionResponseDTO startSession(StartSessionRequestDTO requestDTO);

    SessionResponseDTO stopSession(Long id, StopSessionRequestDTO requestDTO);

    SessionResponseDTO terminateSession(Long id);

    List<SessionResponseDTO> getAllSessions();

    SessionResponseDTO getSessionById(Long id);

    List<SessionResponseDTO> getSessionsByUser(String userId);
}
