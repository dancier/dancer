package net.dancier.dancer.mail.model;

public enum OutgoingMailStatus {
    QUEUED,
    IN_PROGRESS,
    TEMPORARY_FAILED,
    FINALLY_FAILED,
    OK
}
