package com.example.compose_remember.ui.user.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.compose_remember.ui.user.UserListModel
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun UserItem(
    user: UserListModel.User,
    modifier: Modifier = Modifier
) {
    val painter = rememberCoilPainter(request = user.profileImageUrl)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { user.onClick(user.name) }
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .clip(shape = CircleShape)
        )
        Text(
            text = user.name,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
