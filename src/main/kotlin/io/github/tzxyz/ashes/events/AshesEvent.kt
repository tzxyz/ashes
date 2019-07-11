package io.github.tzxyz.ashes.events

import io.github.tzxyz.ashes.models.AshesConnection
import tornadofx.EventBus.RunOn.BackgroundThread
import tornadofx.FXEvent

data class AshesNewConnectionEvent(val connection: AshesConnection): FXEvent(BackgroundThread)

data class AshesUpdateConnectionEvent(val before: AshesConnection, val connection: AshesConnection): FXEvent()

data class AshesScanKeyEvent(val connection: AshesConnection, val keys: List<String>): FXEvent()

data class AshesSendCommandEvent(val cmd: String): FXEvent(BackgroundThread)

data class AshesOpenKeyViewEvent(val key: String): FXEvent()

data class AshesReloadKeysEvent(val connection: AshesConnection): FXEvent()