package br.com.dillmann.fireflymobile

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.dillmann.fireflymobile.business.BusinessModule
import br.com.dillmann.fireflymobile.core.CoreModule
import br.com.dillmann.fireflymobile.database.DatabaseModule
import br.com.dillmann.fireflymobile.firefly.FireflyModule
import br.com.dillmann.fireflymobile.thirdparty.ThirdPartyModule
import br.com.dillmann.fireflymobile.ui.theme.FireflyMobileTheme
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    init {
        startKoin {
            loadKoinModules(module {
                single<Context> { this@MainActivity }
            })

            modules(CoreModule, BusinessModule, DatabaseModule, FireflyModule, ThirdPartyModule)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FireflyMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android", modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FireflyMobileTheme {
        Greeting("Android")
    }
}
