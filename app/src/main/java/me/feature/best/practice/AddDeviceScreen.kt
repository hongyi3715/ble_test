package me.feature.best.practice

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import androidx.compose.runtime.Composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun AddDeviceScreen(viewModel: BleAddDeviceViewModel = hiltViewModel()) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier=Modifier.height(80.dp))

        Button(onClick = {
            viewModel.startScan()
        }) { Text("添加设备")}

        Spacer(modifier = Modifier.height(40.dp))

        Button(onClick = {
        }) { Text("设备解绑")}

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(148.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(20.dp))
         /*   Image(
                modifier = Modifier.weight(2f),
                contentDescription = "AddDevicePhoneIcon"
            )
            Image(
                modifier = Modifier.weight(1f),
                contentDescription = "AddDeviceSearchingIcon"
            )
            Image(
                modifier = Modifier
                    .weight(2f)
                    .padding(20.dp, 0.dp, 0.dp, 0.dp),
                contentDescription = "AddDeviceDeviceIcon"
            )*/

            Spacer(modifier = Modifier.width(20.dp))
        }

        Spacer(modifier = Modifier.height(40.dp))

        LazyColumn {
           items(viewModel.scanResultList){ device->
                DeviceItem(device) {
                    viewModel.connect(device)
                }
           }
        }

    }
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceItem(device: ScanResult, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp,20.dp)
            .clickable {
                onClick.invoke()
            }, horizontalArrangement = Arrangement.Start
    ) {
        Column(Modifier.height(68.dp)) {
            device.device?.name?.let { Text(text = it , fontSize = 16.sp, fontWeight = FontWeight.Bold) }
            device.device?.address?.let { Text(text = it, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
        }
        Text(text = "${device.rssi}" , fontSize = 10.sp, fontWeight = FontWeight.Bold)
//        LocalImage(R.mipmap.avatar, modifier = Modifier.size(28.dp))
    }

}
