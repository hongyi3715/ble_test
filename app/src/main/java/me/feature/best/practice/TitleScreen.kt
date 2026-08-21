package me.feature.best.practice

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun TitleScreen() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
       /* Image(
            painter = painterResource(R.drawable.icon_title_back), "TitleBackIcon",
            modifier = Modifier.padding(18.dp))*/
        Spacer(modifier = Modifier.width(40.dp))

        Text("添加设备")


    }


}
