package io.github.silentdevelopment.relay.dispatch;

public enum CommandDispatchStatus {

    SUCCESS,
    UNKNOWN_COMMAND,
    INVALID_USAGE,
    REQUIREMENT_FAILED,
    ABORTED,
    NO_HANDLER

}