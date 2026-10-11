import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class GameState { START, COUNTDOWN, RUNNING, GAME_OVER }
enum class Mode { COLOR, TEXT }
enum class Choice { INK, WORD }

enum class ColorName(val colorValue: Color) {
    RED(Color.Red),
    BLUE(Color.Blue),
    GREEN(Color.Green),
    YELLOW(Color(0xFFFFA500))
}

@Composable
fun soal3() {
    var gameState by remember { mutableStateOf(GameState.START) }
    var score by remember { mutableIntStateOf(0) }
    var strikes by remember { mutableIntStateOf(0) }
    var bestScore by remember { mutableIntStateOf(0) }

    var currentMode by remember { mutableStateOf(Mode.COLOR) }
    var currentWord by remember { mutableStateOf(ColorName.RED) }
    var currentInk by remember { mutableStateOf(ColorName.BLUE) }
    var isInkLeft by remember { mutableStateOf(true) }

    var timeLeft by remember { mutableIntStateOf(5000) }
    var countdownText by remember { mutableStateOf("3") }


    var questionId by remember { mutableIntStateOf(0) }

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color.LightGray,
        contentColor = Color.Black
    )

    fun generateQuestion() {
        val modes = Mode.values()
        val colors = ColorName.values()

        currentMode = modes[Random.nextInt(modes.size)]
        currentWord = colors[Random.nextInt(colors.size)]
        currentInk = colors[Random.nextInt(colors.size)]

        while (currentInk == currentWord) {
            currentInk = colors[Random.nextInt(colors.size)]
        }

        isInkLeft = Random.nextBoolean()
        timeLeft = 5000
        questionId = questionId + 1
    }

    fun checkAnswer(selectedChoice: Choice) {
        if (gameState != GameState.RUNNING) return

        var isCorrect = false

        if (currentMode == Mode.COLOR) {
            if (selectedChoice == Choice.INK) {
                isCorrect = true
            }
        } else {
            if (selectedChoice == Choice.WORD) {
                isCorrect = true
            }
        }

        if (isCorrect) {
            score = score + 1
            generateQuestion()
        } else {
            strikes = strikes + 1
            if (strikes >= 3) {
                if (score > bestScore) {
                    bestScore = score
                }
                gameState = GameState.GAME_OVER
            } else {
                generateQuestion()
            }
        }
    }

    LaunchedEffect(gameState, questionId) {
        if (gameState == GameState.COUNTDOWN) {
            countdownText = "3"
            delay(700)
            countdownText = "2"
            delay(700)
            countdownText = "1"
            delay(700)
            countdownText = "Start!"
            delay(700)

            score = 0
            strikes = 0
            generateQuestion()
            gameState = GameState.RUNNING

        } else if (gameState == GameState.RUNNING) {
            while (timeLeft > 0) {
                delay(100)
                timeLeft = timeLeft - 100
            }

            if (timeLeft <= 0) {
                strikes = strikes + 1
                if (strikes >= 3) {
                    if (score > bestScore) {
                        bestScore = score
                    }
                    gameState = GameState.GAME_OVER
                } else {
                    generateQuestion()
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (gameState == GameState.START) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Welcome", fontSize = 24.sp)
                Text("to", fontSize = 24.sp)
                Text("Color Word Matching", fontSize = 24.sp)
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { gameState = GameState.COUNTDOWN },
                    colors = buttonColors
                ) {
                    Text("Start Game")
                }
            }
        }

        else if (gameState == GameState.COUNTDOWN) {
            Text(countdownText, fontSize = 48.sp, fontWeight = FontWeight.Bold)
        }

        else if (gameState == GameState.RUNNING) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mode: $currentMode")
                    Text("✅ $score  ❌ $strikes/3")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (currentMode == Mode.COLOR) {
                    Text("Pilih WARNA TINTA", fontWeight = FontWeight.Bold, color = Color.Gray)
                } else {
                    Text("Pilih NAMA KATA", fontWeight = FontWeight.Bold, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text("${timeLeft / 1000} s", fontSize = 24.sp)

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = currentWord.name,
                    color = currentInk.colorValue,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(64.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (isInkLeft) {
                        Button(
                            onClick = { checkAnswer(Choice.INK) },
                            colors = buttonColors
                        ) {
                            Text(currentInk.name)
                        }
                        Button(
                            onClick = { checkAnswer(Choice.WORD) },
                            colors = buttonColors
                        ) {
                            Text(currentWord.name)
                        }
                    } else {
                        Button(
                            onClick = { checkAnswer(Choice.WORD) },
                            colors = buttonColors
                        ) {
                            Text(currentWord.name)
                        }
                        Button(
                            onClick = { checkAnswer(Choice.INK) },
                            colors = buttonColors
                        ) {
                            Text(currentInk.name)
                        }
                    }
                }
            }
        }

        else if (gameState == GameState.GAME_OVER) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Game Over!", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Your Score\n$score", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Best Score\n$bestScore", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { gameState = GameState.COUNTDOWN },
                    colors = buttonColors
                ) {
                    Text("Restart Game")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { gameState = GameState.START },
                    colors = buttonColors
                ) {
                    Text("Exit")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun soal3preview() {
    soal3()
}