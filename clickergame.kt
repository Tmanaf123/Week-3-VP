import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week3vpfinal.R

@Composable
fun CatClickerGame() {

    var coins by remember { mutableIntStateOf(0) }
    var upgradeCost by remember { mutableIntStateOf(10) }
    var coinsPerTap by remember { mutableIntStateOf(1) }

    val nextTapValue = maxOf((coinsPerTap * 1.5).toInt(), coinsPerTap + 1)

    val interactionSource = remember { MutableInteractionSource() }
    val isCatPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(R.drawable.backsoal2),
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text("Your Coins", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    Text(
                        text = "$coins",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00E676)
                    )
                    Text("$coinsPerTap coins per tap", fontSize = 16.sp, color = Color.Gray)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Tap the Cat!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(id = if (isCatPressed) R.drawable.catopen else R.drawable.catclose),
                    contentDescription = "Cat Clicker",
                    modifier = Modifier
                        .size(200.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            coins += coinsPerTap
                        }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isCatPressed) "Meow!" else "Purr~",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text("Give Me Your Coin", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Next upgrade: $nextTapValue coins per tap", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))

                    val canUpgrade = coins >= upgradeCost

                    Button(
                        onClick = {
                            if (canUpgrade) {
                                coins -= upgradeCost
                                coinsPerTap = nextTapValue
                                upgradeCost *= 2
                            }
                        },
                        enabled = canUpgrade,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            disabledContainerColor = Color.LightGray
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (canUpgrade) {
                            Text("Upgrade ($upgradeCost coins)", color = Color.White, fontWeight = FontWeight.Bold)
                        } else {
                            Text("Find ${upgradeCost - coins} more coins", color = Color.DarkGray, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatClickerGamePreview() {
    CatClickerGame()
}