package br.com.dillmann.fireflycompanion.android.accounts

import android.os.Bundle
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import br.com.dillmann.fireflycompanion.android.accounts.components.AccountDetails
import br.com.dillmann.fireflycompanion.android.core.activity.PreconfiguredActivity
import br.com.dillmann.fireflycompanion.android.core.activity.volatile
import br.com.dillmann.fireflycompanion.business.account.Account

class AccountFormActivity : PreconfiguredActivity() {
    private var account: Account? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        account = intent.getSerializableExtra("account", Account::class.java)
    }

    @Composable
    override fun Content(padding: PaddingValues) {
        val state = volatile(account!!)
        AccountDetails(state, resultNotifier)
    }
}
