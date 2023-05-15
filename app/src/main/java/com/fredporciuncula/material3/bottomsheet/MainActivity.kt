package com.fredporciuncula.material3.bottomsheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.fredporciuncula.material3.bottomsheet.ui.theme.Material3BottomSheetTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Material3BottomSheetTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          BottomSheetSample()
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetSample() {
  var openBottomSheet by rememberSaveable { mutableStateOf(false) }
  var skipPartiallyExpanded by remember { mutableStateOf(false) }
  val scope = rememberCoroutineScope()
  val bottomSheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = skipPartiallyExpanded
  )

// App content
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Spacer(modifier = Modifier.weight(1f))
    Row(
      Modifier.toggleable(
        value = skipPartiallyExpanded,
        role = Role.Checkbox,
        onValueChange = { checked -> skipPartiallyExpanded = checked }
      )
    ) {
      Checkbox(checked = skipPartiallyExpanded, onCheckedChange = null)
      Spacer(Modifier.width(16.dp))
      Text("Skip partially expanded State")
    }
    Button(onClick = { openBottomSheet = !openBottomSheet }) {
      Text(text = "Show Bottom Sheet")
    }
    Spacer(modifier = Modifier.weight(1f))
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .background(Color.Red),
    )
  }

// Sheet content
  if (openBottomSheet) {
    ModalBottomSheet(
      onDismissRequest = { openBottomSheet = false },
      sheetState = bottomSheetState,
    ) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Button(
          // Note: If you provide logic outside of onDismissRequest to remove the sheet,
          // you must additionally handle intended state cleanup, if any.
          onClick = {
            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
              if (!bottomSheetState.isVisible) {
                openBottomSheet = false
              }
            }
          }
        ) {
          Text("Hide Bottom Sheet")
        }
      }
      var text by remember { mutableStateOf("") }
      OutlinedTextField(value = text, onValueChange = { text = it })
      LazyColumn {
        items(50) {
          ListItem(
            headlineContent = { Text("Item $it") },
            leadingContent = {
              Icon(
                Icons.Default.Favorite,
                contentDescription = "Localized description"
              )
            }
          )
        }
      }
    }
  }
}
