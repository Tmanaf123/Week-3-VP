import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random
import com.example.week3vpfinal.R

enum class ScreenState { START, WAITING, READY, EARLY_TAP, TRIAL_RESULT, FINAL_RESULT }

@Composable
fun soal1() {
    var state by remember { mutableStateOf(ScreenState.START) }

    var currentTrial by remember { mutableIntStateOf(1) }
    var trialResults by remember { mutableStateOf(mutableListOf<Long?>(null, null, null)) }
    var startTime by remember { mutableLongStateOf(0L) }
    var lastReactionTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(state) {
        if (state == ScreenState.WAITING) {
            val randomDelay = Random.nextLong(500, 4500)
            delay(randomDelay)
            startTime = System.currentTimeMillis()
            state = ScreenState.READY
        }
    }

    val backgroundColor = when (state) {
        ScreenState.START -> Color.Cyan
        ScreenState.WAITING -> Color.LightGray
        ScreenState.READY, ScreenState.TRIAL_RESULT -> Color.Green
        ScreenState.EARLY_TAP -> Color.Red
        ScreenState.FINAL_RESULT -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clickable {
                when (state) {
                    ScreenState.START -> {
                        currentTrial = 1
                        trialResults = mutableListOf(null, null, null)
                        state = ScreenState.WAITING
                    }
                    ScreenState.WAITING -> {
                        state = ScreenState.EARLY_TAP
                    }
                    ScreenState.READY -> {
                        lastReactionTime = System.currentTimeMillis() - startTime
                        val updatedResults = trialResults.toMutableList()
                        updatedResults[currentTrial - 1] = lastReactionTime
                        trialResults = updatedResults

                        if (currentTrial < 3) {
                            state = ScreenState.TRIAL_RESULT
                        } else {
                            state = ScreenState.FINAL_RESULT
                        }
                    }
                    ScreenState.EARLY_TAP -> {
                        state = ScreenState.WAITING
                    }
                    ScreenState.TRIAL_RESULT -> {
                        currentTrial++
                        state = ScreenState.WAITING
                    }
                    ScreenState.FINAL_RESULT -> {
                        state = ScreenState.START
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            ScreenState.START -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("Reaction", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(48.dp))
                    Icon(painter = painterResource(id = R.drawable.baseline_flash_on_24), contentDescription = "Lightning Bolt", tint = Color.White, modifier = Modifier.size(120.dp))
                    Spacer(modifier = Modifier.height(48.dp))
                    Text("Test", fontSize = 24.sp, fontWeight = FontWeight.Medium, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Click to Start", fontSize = 16.sp, color = Color.White)
                }
            }
            ScreenState.WAITING -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("Get Ready", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(48.dp))
                    Icon(painter = painterResource(id = R.drawable.baseline_warning_24), contentDescription = "Warning", tint = Color.White, modifier = Modifier.size(120.dp))
                    Spacer(modifier = Modifier.height(48.dp))
                    Text("Wait for green light...", fontSize = 24.sp, fontWeight = FontWeight.Medium, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("DON'T CLICK YET!", fontSize = 16.sp, color = Color.White)
                }
            }
            ScreenState.READY -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("GO!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(48.dp))
                    Icon(painter = painterResource(id = R.drawable.outline_accessible_forward_24), contentDescription = "Go", tint = Color.White, modifier = Modifier.size(120.dp))
                    Spacer(modifier = Modifier.height(48.dp))
                    Text("CLICK NOW!", fontSize = 24.sp, fontWeight = FontWeight.Medium, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("TAP AS FAST AS YOU CAN!", fontSize = 16.sp, color = Color.White)
                }
            }
            ScreenState.EARLY_TAP -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("FAIL!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.thumbs),
                        contentDescription = "Trial Complete",
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("You clicked too early, TRY TO FOLLOW THE RULE BRO")
                    Spacer(modifier=Modifier.height(24.dp))
                    Text("Click anywhere to try Trial $currentTrial again", fontSize = 18.sp, color = Color.White)
                    ResultsTable(trialResults)
                }
            }
            ScreenState.TRIAL_RESULT -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Trial $currentTrial Complete!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(32.dp))
                    Image(
                        painter = painterResource(id = R.drawable.checkmarkrea),
                        contentDescription = "Trial Complete",
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("Time: ${lastReactionTime}ms", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Continue to Trial ${currentTrial + 1}", fontSize = 16.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(32.dp))

                    ResultsTable(trialResults)
                }
            }
            ScreenState.FINAL_RESULT -> {
                val validScores = trialResults.filterNotNull()
                val averageScore = if (validScores.isNotEmpty()) validScores.average().toInt() else 0

                val (feedbackMessage, imageResource, finalBackgroundColor) = when {
                    averageScore < 180 -> Triple("DANG YOU ARE SO\nFAST BRO!", R.drawable.image1, Color(0xFF00E676))
                    averageScore < 280 -> Triple("YOUR REFLEX IS\nGOOD", R.drawable.iamge4, Color(0xFF2196F3))
                    averageScore < 450 -> Triple("MEH LIKE ANY OTHER\nPERSON", R.drawable.image3, Color(0xFFFF9800))
                    else -> Triple("YOU LIKE A SNAIL\nBRO", R.drawable.iamge2, Color(0xFFE57373))
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(finalBackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = feedbackMessage,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Spacer(modifier = Modifier.height(48.dp))

                        Image(
                            painter = painterResource(id = imageResource),
                            contentDescription = "Result Image",
                            modifier = Modifier.size(150.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(48.dp))

                        Text("Average: ${averageScore}ms", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Click to Start New Test", fontSize = 16.sp, color = Color.White)
                        Spacer(modifier = Modifier.height(32.dp))

                        ResultsTable(results = trialResults, average = averageScore)
                    }
                }
            }
        }
    }
}

@Composable
fun ResultsTable(results: List<Long?>, average: Int? = null) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text("Trial Results", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("1", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                Text("2", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                Text("3", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(results[0]?.let { "${it}ms" } ?: "-", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(results[1]?.let { "${it}ms" } ?: "-", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(results[2]?.let { "${it}ms" } ?: "-", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }

            if (average != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Average Score", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                Spacer(modifier = Modifier.height(4.dp))
                Text("${average}ms", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF6D00))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun soal1prewview() {
    soal1()
}