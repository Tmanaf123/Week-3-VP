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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

enum class State { INITIAL, PICK, REVEAL, FINISHED }
enum class Result(val text: String) { WIN("You Win!"), LOSE("You Lose"), DRAW("Draw") }
enum class Rps(val emoji: String) { ROCK("✊"), PAPER("✋"), SCISSORS("✌") }

@Composable
fun soal4() {
    val n = 5
    val targetScore = (n / 2) + 1

    var gameState by rememberSaveable { mutableStateOf(State.INITIAL) }
    var playerScore by rememberSaveable { mutableStateOf(0) }
    var cpuScore by rememberSaveable { mutableStateOf(0) }
    var bestScore by rememberSaveable { mutableStateOf(0) }

    var playerMove by rememberSaveable { mutableStateOf(Rps.ROCK) }
    var cpuMove by rememberSaveable { mutableStateOf(Rps.ROCK) }
    var roundResult by rememberSaveable { mutableStateOf(Result.DRAW) }

    var buttonOrder by rememberSaveable { mutableStateOf(Rps.values().toList()) }

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color.LightGray,
        contentColor = Color.Black
    )

    fun playRound(choice: Rps) {
        playerMove = choice
        cpuMove = Rps.values().random()

        if (playerMove == cpuMove) {
            roundResult = Result.DRAW
        } else if (
            (playerMove == Rps.ROCK && cpuMove == Rps.SCISSORS) ||
            (playerMove == Rps.PAPER && cpuMove == Rps.ROCK) ||
            (playerMove == Rps.SCISSORS && cpuMove == Rps.PAPER)
        ) {
            roundResult = Result.WIN
            playerScore = playerScore + 1
        } else {
            roundResult = Result.LOSE
            cpuScore = cpuScore + 1
        }

        gameState = State.REVEAL
    }

    LaunchedEffect(gameState) {
        if (gameState == State.REVEAL) {
            delay(700)

            if (playerScore >= targetScore || cpuScore >= targetScore) {
                val scoreDiff = playerScore - cpuScore
                if (scoreDiff > bestScore) {
                    bestScore = scoreDiff
                }
                gameState = State.FINISHED
            } else {
                buttonOrder = Rps.values().toList().shuffled()
                gameState = State.PICK
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("🧑 $playerScore - $cpuScore 🤖", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Best of $n", fontSize = 16.sp, color = Color.Gray)
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (gameState == State.INITIAL) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Rock • Paper • Scissors", fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            playerScore = 0
                            cpuScore = 0
                            buttonOrder = Rps.values().toList().shuffled()
                            gameState = State.PICK
                        },
                        colors = buttonColors,
                        modifier = Modifier.semantics { contentDescription = "Start Button" }
                    ) {
                        Text("Start")
                    }
                }
            }

            else if (gameState == State.PICK) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Pick your move!", fontSize = 18.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("❔ VS ❔", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(48.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { playRound(buttonOrder[0]) },
                            colors = buttonColors,
                            modifier = Modifier.semantics { contentDescription = buttonOrder[0].name }
                        ) {
                            Text(buttonOrder[0].name)
                        }
                        Button(
                            onClick = { playRound(buttonOrder[1]) },
                            colors = buttonColors,
                            modifier = Modifier.semantics { contentDescription = buttonOrder[1].name }
                        ) {
                            Text(buttonOrder[1].name)
                        }
                        Button(
                            onClick = { playRound(buttonOrder[2]) },
                            colors = buttonColors,
                            modifier = Modifier.semantics { contentDescription = buttonOrder[2].name }
                        ) {
                            Text(buttonOrder[2].name)
                        }
                    }
                }
            }

            else if (gameState == State.REVEAL) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${playerMove.emoji} VS ${cpuMove.emoji}",
                        fontSize = 48.sp,
                        modifier = Modifier.semantics { contentDescription = "Reveal Icons" }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = roundResult.text,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.semantics { contentDescription = "Round Result" }
                    )
                }
            }

            else if (gameState == State.FINISHED) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (playerScore >= targetScore) {
                        Text("You Win the Match!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Text("You Lose the Match", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Best Score: $bestScore", fontSize = 16.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(48.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Button(
                            onClick = {
                                playerScore = 0
                                cpuScore = 0
                                buttonOrder = Rps.values().toList().shuffled()
                                gameState = State.PICK
                            },
                            colors = buttonColors,
                            modifier = Modifier.semantics { contentDescription = "Restart Button" }
                        ) {
                            Text("Restart")
                        }
                        Button(
                            onClick = {
                                playerScore = 0
                                cpuScore = 0
                                gameState = State.INITIAL
                            },
                            colors = buttonColors,
                            modifier = Modifier.semantics { contentDescription = "Exit Button" }
                        ) {
                            Text("Exit")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun soial4preview() {
    soal4()
}