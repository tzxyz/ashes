package org.tzxyz.ashes.viewmodels

import org.tzxyz.ashes.models.AshesConnection
import tornadofx.ItemViewModel

class AshesConnectionItemViewModel: ItemViewModel<AshesConnection>() {
    val name = bind(AshesConnection::name)
    val host = bind(AshesConnection::host)
    val db = bind(AshesConnection::db)
    val port = bind(AshesConnection::port)
    val pass = bind(AshesConnection::password)

    override fun onCommit() {
        item = AshesConnection(name.value, host.value, db.value, port.value, pass.value)
    }
}