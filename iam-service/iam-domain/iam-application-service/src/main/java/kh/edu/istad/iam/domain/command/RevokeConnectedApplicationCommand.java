package kh.edu.istad.iam.domain.command;

public record RevokeConnectedApplicationCommand(String clientId, String username, String userUuid) {}
