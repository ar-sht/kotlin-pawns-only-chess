package chess

import kotlin.io.println as println

fun main() {
    println("Pawns-Only Chess")
    println("First Player's name:")
    val player1 = readln()
    println("Second Player's name:")
    val player2 = readln()
    val border = "  +---+---+---+---+---+---+---+---+"
    val letterRow = "    a   b   c   d   e   f   g   h  "
    var move: String
    var whitePawnPast = mutableListOf<String>()
    var blackPawnPast = mutableListOf<String>()
    val whitePawnPositions = mutableListOf("a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2")
    val blackPawnPositions = mutableListOf("a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7")
    var whiteMoveCombos: List<String>
    var blackMoveCombos: List<String>
    var whiteCaptures: List<String>
    var blackCaptures: List<String>
    val regex = Regex("""[a-h][1-9][a-h][1-9]""")
    fun executeMove(move: String, color: String, capture: Boolean) {
        val endingPos = move.substring(2)
        val startingPos = move.substring(0, 2)
        if (color[0].uppercaseChar() == 'W') {
            whitePawnPast = whitePawnPositions.toMutableList()
            whitePawnPositions[whitePawnPositions.indexOf(startingPos)] = endingPos
            if (capture) {
                // checks for normal capture
                if (endingPos in blackPawnPositions) {
                    // removes taken piece, retains its spot in lists so everything doesn't break
                    blackPawnPositions[blackPawnPositions.indexOf(endingPos)] = ""
                }
                // checks for en passant
                else if (endingPos[0].toString() + (endingPos[1] - 1).toString() in blackPawnPositions) {
                    // removes taken piece, retains its spot in lists so everything doesn't break
                    blackPawnPositions[blackPawnPositions.indexOf(endingPos[0].toString() + (endingPos[1] - 1).toString())] = ""
                }
            }
        } else {
            blackPawnPast = blackPawnPositions.toMutableList()
            blackPawnPositions[blackPawnPositions.indexOf(startingPos)] = endingPos
            if (capture) {
                // checks for normal capture
                if (endingPos in whitePawnPositions) {
                    // removes taken piece, retains its spot in lists so everything doesn't break
                    whitePawnPositions[whitePawnPositions.indexOf(endingPos)] = ""
                }
                // checks for en passant
                else if (endingPos[0].toString() + (endingPos[1] + 1).toString() in whitePawnPositions) {
                    // removes taken piece, retains its spot in lists so everything doesn't break
                    whitePawnPositions[whitePawnPositions.indexOf(endingPos[0].toString() + (endingPos[1] + 1).toString())]
                }
            }
        }
        // iterates through each row
        for (row in 8 downTo 1) {
            // prints border thing
            println(border)
            // prints row number
            print("$row ")
            // iterates through columns
            for (column in 1..8) {
                // checks if there is a white piece on this coordinate
                if (((column.toChar() + 96).toString() + row) in whitePawnPositions) {
                    // prints white piece on a square
                    print("| W ")
                }
                // checks if there is a black piece on this coordinate
                else if (((column.toChar() + 96).toString() + row) in blackPawnPositions) {
                    // prints black piece on a square
                    print("| B ")
                } else {
                    // prints empty square
                    print("|   ")
                }
            }
            //closes final square and starts new line
            print("|\n")
        }
        // prints final bottom border
        println(border)
        // prints the letters to label the files
        println(letterRow)
    }
    // sets up board
    executeMove("a2a2", "white", false)
    // allows for alternation
    var i = 1
    // game begins, ends when 'exit' typed
    while (true) {
        // resets move lists so only current possible moves are considered
        whiteMoveCombos = listOf()
        whiteCaptures = listOf()
        blackMoveCombos = listOf()
        blackCaptures = listOf()
        var whiteGone = true
        var blackGone = true
        // checks for losing 'all my pawns are gone' conditions
        for (pawn in whitePawnPositions) {
            if (pawn != "") whiteGone = false
        }
        for (pawn in blackPawnPositions) {
            if (pawn != "") blackGone = false
        }
        if (whiteGone) {
            println("Black Wins!")
            println("Bye!")
            break
        }
        if (blackGone) {
            println("White Wins!")
            println("Bye!")
            break
        }
        // checks that it's white's move
        if (i % 2 == 1) {
            for (pawn in blackPawnPositions) {
                // checks for winning pawn on back rank condition
                if (pawn != "" && pawn.toCharArray()[1] == '1') {
                    println("Black Wins!")
                    println("Bye!")
                    break
                }
            }
            var whiteHasNoMoves = true
            // iterates through each pawn
            for (space in whitePawnPositions) {
                // makes sure the pawn still exists
                if (space == "") {
                    continue
                }
                // sets lists for possible forward moves and captures
                var possibleWhiteForwardMoves = ""
                var possibleWhiteCaptureMoves = ""
                // checks if square in front is clear
                if ("${space[0]}${space[1] + 1}" !in blackPawnPositions && "${space[0]}${space[1] + 1}" !in whitePawnPositions) {
                    // checks if pawn is on 2nd rank and clear to move twice
                    if ("${space[0]}${space[1] + 2}" !in blackPawnPositions && "${space[0]}${space[1] + 2}" !in whitePawnPositions && space[1] == '2') {
                        // adds moving two squares to the possibilities
                        possibleWhiteForwardMoves += space[0].toString() + (space[1] + 2)
                    }
                    // adds moving just one square to the possibilities because it's not on the 2nd rank and/or isn't clear to move 2
                    possibleWhiteForwardMoves += space[0].toString() + (space[1] + 1)
                }
                // normal captures
                if ("${space[0] - 1}${space[1] + 1}" in blackPawnPositions) {
                    possibleWhiteCaptureMoves += (space[0] - 1) + (space[1] + 1).toString()
                } else if ("${space[0] + 1}${space[1] + 1}" in blackPawnPositions) {
                    possibleWhiteCaptureMoves += (space[0] + 1) + (space[1] + 1).toString()
                }
                // en passant
                if ("${space[0] - 1}7" in blackPawnPast && "${space[0] - 1}5" in blackPawnPositions && space[1] == '5') {
                    possibleWhiteCaptureMoves += (space[0] - 1).toString() + (space[1] + 1).toString()
                } else if ("${space[0] + 1}7" in blackPawnPast && "${space[0] + 1}5" in blackPawnPositions && space[1] == '5') {
                    possibleWhiteCaptureMoves += (space[0] + 1).toString() + (space[1] + 1).toString()
                }
                // puts current position and string with possible moves into lists with those for all pawns
                whiteMoveCombos = whiteMoveCombos.plus(space).plus(possibleWhiteForwardMoves)
                whiteCaptures = whiteCaptures.plus(space).plus(possibleWhiteCaptureMoves)
                if (possibleWhiteForwardMoves.isEmpty() && possibleWhiteCaptureMoves.isEmpty()) {
                    continue
                }
                whiteHasNoMoves = false
            }
            // checks if white has no moves
            if (whiteHasNoMoves) {
                println("Stalemate!")
                println("Bye!")
                break
            }
            // gets player's move
            println("${player1}'s turn:")
            move = readln()
            // listens for 'exit'
            if (move == "exit") {
                println("Bye!")
                break
            }
            // makes sure the move is in the right format
            if (!(move.matches(regex))) {
                println("Invalid Input")
                continue
            }
            // makes sure pawn exists in spot specified
            else if (move.substring(0, 2) !in whiteMoveCombos && move.substring(0, 2) !in whiteCaptures) {
                println("No white pawn at ${move.substring(0, 2)}")
                continue
            }
            // checks if chosen forward move is available
            if (move.substring(2) in whiteMoveCombos[whiteMoveCombos.indexOf(move.substring(0, 2)) + 1]) {
                // plays move and creates new board based on it, iterates to white's turn
                executeMove(move, "white", false)
                i++
            }
            // checks if chosen capture is available
            else if (move.substring(2) in whiteCaptures[whiteCaptures.indexOf(move.substring(0, 2)) + 1]) {
                // plays capture and creates new board, iterates to white's turn
                executeMove(move, "white", true)
                i++
            } else {
                println("Invalid Input")
            }
        } else {
            for (pawn in whitePawnPositions) {
                // checks for winning pawn on back rank condition
                if (pawn != "" && pawn.toCharArray()[1] == '8') {
                    println("White Wins!")
                    println("Bye!")
                    break
                }
            }
            var blackHasNoMoves = true
            // iterates through all black pawns
            for (space in blackPawnPositions) {
                // makes sure pawn still exists
                if (space == "") {
                    continue
                }
                // sets lists for possible forward moves and captures
                var possibleBlackForwardMoves = ""
                var possibleBlackCaptureMoves = ""
                // checks that next square is open
                if ("${space[0]}${space[1] - 1}" !in blackPawnPositions && "${space[0]}${space[1] - 1}" !in whitePawnPositions) {
                    // checks if pawn is on 7th rank and next two squares are open
                    if ("${space[0]}${space[1] - 2}" !in blackPawnPositions && "${space[0]}${space[1] - 2}" !in whitePawnPositions && space[1] == '7') {
                        // adds moving two squares to the possibilities
                        possibleBlackForwardMoves += space[0].toString() + (space[1] - 2)
                    }
                    // adds moving just one square to the possibilities
                    possibleBlackForwardMoves += space[0].toString() + (space[1] - 1)
                }
                // normal captures
                if ("${space[0] - 1}${space[1] - 1}" in whitePawnPositions) {
                    possibleBlackCaptureMoves += (space[0] - 1) + (space[1] - 1).toString()
                } else if ("${space[0] + 1}${space[1] - 1}" in whitePawnPositions) {
                    possibleBlackCaptureMoves += (space[0] + 1) + (space[1] - 1).toString()
                }
                // en passant
                if ("${space[0] - 1}2" in whitePawnPast && "${space[0] - 1}4" in whitePawnPositions && space[1] == '4') {
                    possibleBlackCaptureMoves += (space[0] - 1).toString() + (space[1] - 1).toString()
                } else if ("${space[0] + 1}2" in whitePawnPast && "${space[0] + 1}4" in whitePawnPositions && space[1] == '4') {
                    possibleBlackCaptureMoves += (space[0] + 1).toString() + (space[1] - 1).toString()
                }
                // adds possibilities back into the lists with those for all the pawns
                blackMoveCombos = blackMoveCombos.plus(space).plus(possibleBlackForwardMoves)
                blackCaptures = blackCaptures.plus(space).plus(possibleBlackCaptureMoves)
                if (possibleBlackForwardMoves.isEmpty() && possibleBlackCaptureMoves.isEmpty()) {
                    continue
                }
                blackHasNoMoves = false
            }
            // checks if black has no moves
            if (blackHasNoMoves) {
                println("Stalemate!")
                println("Bye!")
                break
            }
            // asks for black's move
            println("${player2}'s turn:")
            move = readln()
            // listens for 'exit' to break the loop
            if (move == "exit") {
                println("Bye!")
                break
            }
            if (!(move.matches(regex))) {
                println("Invalid Input")
                continue
            }
            // checks that pawn that is to be moved exists
            else if (move.substring(0, 2) !in blackMoveCombos && move.substring(0, 2) !in blackCaptures) {
                println("No black pawn at ${move.substring(0, 2)}")
                continue
            }
            // checks that pawn can make forward move to where the player wants it
            if (move.substring(2) in blackMoveCombos[blackMoveCombos.indexOf(move.substring(0, 2)) + 1]) {
                // plays move and updates board, iterates to white's turn
                executeMove(move, "black", false)
                i++
            }
            // checks that pawn can make capture where the player wants it to
            else if (move.substring(2) in blackCaptures[blackCaptures.indexOf(move.substring(0, 2)) + 1]) {
                // plays capture and updates board, iterates to white's turn
                executeMove(move, "black", true)
                i++
            }
            else {
                println("Invalid Input")
            }
        }
    }
}
