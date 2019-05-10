package org.tzxyz.ashes.events

import org.tzxyz.ashes.models.AshesConnection
import tornadofx.EventBus.RunOn.BackgroundThread
import tornadofx.FXEvent

data class AshesNewConnectionEvent(val connection: AshesConnection): FXEvent(BackgroundThread)

data class AshesUpdateConnectionEvent(val before: AshesConnection, val connection: AshesConnection): FXEvent()

data class AshesScanKeyEvent(val connection: AshesConnection, val keys: List<String>): FXEvent()

data class AshesSendCommandEvent(val cmd: String): FXEvent(BackgroundThread)

data class AshesOpenKeyViewEvent(val key: String): FXEvent()