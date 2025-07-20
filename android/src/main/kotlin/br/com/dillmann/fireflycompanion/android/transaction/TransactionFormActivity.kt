package br.com.dillmann.fireflycompanion.android.transaction

import android.os.Bundle
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import br.com.dillmann.fireflycompanion.android.core.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.transaction.components.TransactionForm
import br.com.dillmann.fireflycompanion.business.transaction.Transaction

class TransactionFormActivity : PreconfiguredActivity() {
    private var transaction: Transaction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transaction = intent.getSerializableExtra("transaction", Transaction::class.java)
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    override fun Content(padding: PaddingValues) {
        TransactionForm(transaction, ::finish, ::setResult)
    }
}
