// Importamos las clases necesarias para entrada del usuario y generación de números aleatorios
import java.util.Scanner;
import java.util.Random;

/**
 * Clase que representa a un jugador.
 * Almacena el nombre del jugador y su puntaje total obtenido en su partida.
 */
class Player {
    String name;   // Nombre del jugador
    int score;     // Puntaje acumulado

    /**
     * Constructor de la clase Player.
     * @param name Nombre del jugador.
     * @param score Puntaje obtenido por el jugador.
     */
    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Representación en cadena del jugador para mostrar en consola.
     * @return Cadena con el formato: "nombre - X puntos"
     */
    @Override
    public String toString() {
        return name + " - " + score + " puntos";
    }
}

/**
 * Clase que representa un nodo en una lista doblemente enlazada.
 * Cada nodo contiene un objeto Player y referencias al nodo anterior y siguiente.
 */
class Node {
    Player player;  // Dato almacenado: un jugador
    Node next;      // Enlace al siguiente nodo
    Node prev;      // Enlace al nodo anterior

    /**
     * Constructor del nodo.
     * @param player Objeto Player que se almacenará en este nodo.
     */
    public Node(Player player) {
        this.player = player;
        this.next = null;
        this.prev = null;
    }
}

/**
 * Clase que implementa una lista doblemente enlazada para gestionar el ranking de jugadores.
 * Los jugadores se mantienen ordenados de mayor a menor puntaje.
 * Está diseñada para contener exactamente a los 5 jugadores de una ronda.
 */
class DoublyLinkedList {
    private Node head;  // Primer nodo (mejor puntaje)
    private Node tail;  // Último nodo (menor puntaje)

    /**
     * Constructor de la lista. Inicializa una lista vacía.
     */
    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
    }

    /**
     * Inserta un nuevo jugador en la lista, manteniendo el orden descendente por puntaje.
     * Si hay empate, el nuevo jugador se coloca antes (más arriba en el ranking).
     * @param newPlayer Jugador a insertar.
     */
    public void insertSorted(Player newPlayer) {
        Node newNode = new Node(newPlayer);

        // Caso 1: Lista vacía
        if (head == null) {
            head = tail = newNode;
            return;
        }

        // Caso 2: Nuevo jugador tiene puntaje mayor o igual al primero
        if (newPlayer.score >= head.player.score) {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
            return;
        }

        // Caso 3: Buscar posición correcta
        Node current = head;
        while (current != null && newPlayer.score < current.player.score) {
            current = current.next;
        }

        // Caso 3a: Insertar al final
        if (current == null) {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        } 
        // Caso 3b: Insertar en medio
        else {
            Node previous = current.prev;
            previous.next = newNode;
            newNode.prev = previous;
            newNode.next = current;
            current.prev = newNode;
        }
    }

    /**
     * Limita la lista a los 5 mejores jugadores.
     * En este juego, siempre serán 5, pero el método asegura que no haya más.
     */
    public void keepTop5() {
        if (head == null) return;

        Node current = head;
        int count = 0;
        // Avanzar hasta el quinto nodo (índice 4)
        while (current != null && count < 4) {
            current = current.next;
            count++;
        }

        // Si hay más de 5 nodos, cortar la lista
        if (current != null && current.next != null) {
            current.next = null;
            tail = current;
        }
    }

    /**
     * Muestra en consola el ranking de los 5 jugadores.
     */
    public void displayTop5() {
        System.out.println("\n--- RANKING FINAL (TOP 5) ---");
        Node current = head;
        int position = 1;
        while (current != null && position <= 5) {
            System.out.println(position + ". " + current.player);
            current = current.next;
            position++;
        }
        if (head == null) {
            System.out.println("No hay jugadores registrados.");
        }
    }

    /**
     * Devuelve el jugador con el puntaje más alto (el ganador).
     * @return El primer jugador en la lista (head), o null si la lista está vacía.
     */
    public Player getWinner() {
        if (head != null) {
            return head.player;
        }
        return null;
    }
}

/**
 * Clase principal del juego "Sumas Rápidas".
 * Permite que 5 jugadores participen en una misma ronda.
 * Cada jugador juega su propia partida, y al final se muestra el ranking y el ganador.
 */
public class Sumas_Rápidas {
    // Escáner para leer la entrada del usuario
    private static final Scanner scanner = new Scanner(System.in);
    // Generador de números aleatorios para crear las sumas
    private static final Random random = new Random();

    /**
     * Método principal. Punto de entrada del programa.
     */
    public static void main(String[] args) {
        System.out.println("JUEGO DE SUMAS RAPIDAS - 5 JUGADORES");
        System.out.println("Cada jugador jugara su turno. Buena suerte!\n");

        // Creamos una lista para almacenar el ranking de esta ronda (5 jugadores)
        DoublyLinkedList rankingRonda = new DoublyLinkedList();

        // Bucle para que jueguen exactamente 5 jugadores
        for (int jugadorNum = 1; jugadorNum <= 5; jugadorNum++) {
            System.out.println("=== JUGADOR " + jugadorNum + " ===");
            System.out.print("Ingresa tu nombre: ");
            String nombre = scanner.nextLine().trim();

            // Validar nombre: si está vacío, asignar nombre predeterminado
            if (nombre.isEmpty()) {
                nombre = "Jugador" + jugadorNum;
                System.out.println("Nombre invalido. Se usara: " + nombre);
            }

            // Inicializar variables para la partida de este jugador
            int nivel = 1;                // Nivel actual
            int tiempoLimite = 10;        // Tiempo inicial en segundos
            int puntajeTotal = 0;         // Puntaje acumulado
            boolean juegoActivo = true;   // Controla si el jugador sigue en juego

            // Bucle de niveles: el jugador avanza mientras responda correctamente
            while (juegoActivo) {
                System.out.println("\n--- Nivel " + nivel + " ---");
                System.out.println("Tienes " + tiempoLimite + " segundos por suma.");
                int aciertos = 0; // Contador de respuestas correctas en el nivel

                // Cada nivel tiene 5 sumas
                for (int i = 0; i < 5; i++) {
                    // Generar dos numeros aleatorios entre 1 y 50
                    int a = random.nextInt(50) + 1;
                    int b = random.nextInt(50) + 1;
                    int resultadoCorrecto = a + b;

                    System.out.println("\nSuma: " + a + " + " + b + " = ?");

                    // Registrar tiempo de inicio
                    long startTime = System.currentTimeMillis();

                    // Leer respuesta del usuario
                    String input = "";
                    if (scanner.hasNextLine()) {
                        input = scanner.nextLine().trim();
                    }

                    // Registrar tiempo de fin
                    long endTime = System.currentTimeMillis();
                    double tiempoTranscurrido = (endTime - startTime) / 1000.0;

                    // Verificar si se excedio el tiempo limite
                    if (tiempoTranscurrido > tiempoLimite) {
                        System.out.printf("Se acabo el tiempo! (%.2f segundos)\n", tiempoTranscurrido);
                        juegoActivo = false;
                        break;
                    }

                    // Verificar si la entrada esta vacia
                    if (input.isEmpty()) {
                        System.out.println("Entrada vacia. Turno terminado.");
                        juegoActivo = false;
                        break;
                    }

                    // Intentar convertir la entrada a numero entero
                    try {
                        int respuesta = Integer.parseInt(input);
                        if (respuesta == resultadoCorrecto) {
                            System.out.println("Correcto!");
                            puntajeTotal += 100; // Sumar 100 puntos por acierto
                            aciertos++;
                        } else {
                            System.out.println("Incorrecto. La respuesta correcta era: " + resultadoCorrecto);
                            juegoActivo = false;
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada no valida. Debes ingresar un numero entero.");
                        juegoActivo = false;
                        break;
                    }
                }

                // Si el jugador completo las 5 sumas, avanza de nivel
                if (juegoActivo && aciertos == 5) {
                    System.out.println("\nCompletaste el nivel " + nivel + "!");
                    nivel++;
                    // Reducir el tiempo limite en 2 segundos, minimo 2 segundos
                    tiempoLimite = Math.max(2, tiempoLimite - 2);
                } else {
                    // Si fallo en algun punto, termina su turno
                    break;
                }
            }

            // Mostrar resumen del turno del jugador
            System.out.println("\nTurno de " + nombre + " finalizado.");
            System.out.println("Puntaje obtenido: " + puntajeTotal + " puntos.\n");

            // Agregar al ranking de la ronda
            rankingRonda.insertSorted(new Player(nombre, puntajeTotal));
        }

        // Asegurar que solo haya 5 jugadores en el ranking (por seguridad)
        rankingRonda.keepTop5();

        // Mostrar el ranking final
        rankingRonda.displayTop5();

        // Determinar y anunciar al ganador
        Player ganador = rankingRonda.getWinner();
        if (ganador != null) {
            System.out.println("\nEL GANADOR ES: " + ganador.name + " con " + ganador.score + " puntos!");
        }

        // Mensaje de despedida
        System.out.println("\nGracias a todos por jugar.");
        scanner.close(); // Liberar el recurso del escaner
    }
}