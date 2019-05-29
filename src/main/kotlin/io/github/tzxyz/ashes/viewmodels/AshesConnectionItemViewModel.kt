package io.github.tzxyz.ashes.viewmodels

import io.github.tzxyz.ashes.models.AshesConnection
import tornadofx.ItemViewModel
import java.util.*

abstract class AshesConnectionItemViewModel: ItemViewModel<AshesConnection>() {
    val id = bind(AshesConnection::id, defaultValue = UUID.randomUUID().toString())
    val name = bind(AshesConnection::name)
    val host = bind(AshesConnection::host)
    val db = bind(AshesConnection::db, defaultValue = 0)
    val port = bind(AshesConnection::port, defaultValue = 6379)
    val pass = bind(AshesConnection::password)

    override fun onCommit() {
        item = AshesConnection(if (id.value == null) UUID.randomUUID().toString() else id.value, name.value, host.value, port.value, db.value, pass.value)
    }
}

class AshesNewConnectionItemViewModel: AshesConnectionItemViewModel()
class AshesEditConnectionItemViewModel: AshesConnectionItemViewModel()