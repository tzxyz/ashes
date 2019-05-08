package org.tzxyz.ashes.viewmodels

import org.tzxyz.ashes.models.AshesConnection
import tornadofx.ItemViewModel
import java.util.*

class AshesConnectionItemViewModel: ItemViewModel<AshesConnection>() {
    val id = bind(AshesConnection::id, defaultValue = UUID.randomUUID().toString())
    val name = bind(AshesConnection::name)
    val host = bind(AshesConnection::host)
    val db = bind(AshesConnection::db, defaultValue = 0)
    val port = bind(AshesConnection::port, defaultValue = 6379)
    val pass = bind(AshesConnection::password)

    override fun onCommit() {
        item = AshesConnection(id.value, name.value, host.value, port.value, db.value, pass.value)
    }
}