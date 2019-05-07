package org.tzxyz.ashes.events

import org.tzxyz.ashes.models.AshesConnection
import tornadofx.EventBus.RunOn.BackgroundThread
import tornadofx.FXEvent

class AshesNewConnectionEvent(val connection: AshesConnection): FXEvent(BackgroundThread)

class AshesScanKeyEvent(val connection: AshesConnection, val keys: List<String>): FXEvent()

class AshesSendCommandEvent(val cmd: String): FXEvent(BackgroundThread)

class AshesOpenKeyViewEvent(val key: String): FXEvent()