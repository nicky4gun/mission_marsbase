# Mission Mars-base
## Forståelse af Systemet
**Hvad er serverens ansvar?**
- modtage, behandle og besvare anmodninger
  
**Hvad er sensor-klientens ansvar?**
- sende sensordata til serveren, modtage svar
  
**Hvordan håndteres flere klienter samtidig?**
- multithredning server
  
**Hvor skal ExecutorService anvendes?**
 - serveren
   
**Hvor bør en sensor-forbindelse håndteres?**
 - i en server listner
   
**Hvilke data sender klient & server til hinanden?**
 - målte værdier  og alarmer
   
## AI-agent
**En opgave I gav agenten:**

SERVER Issue #2

**Hvorfor var opgaven afgrænset på den måde?**

- Brug ExecutorService med fx 5 tråde
- For hver klient: læs linje for linje og parse typen + værdi
- Tjek mod grænser og skriv til logfil (mars.log)
- Hvis alarm: skriv alarm i konsol og til klient

vi viste at der kun måtte være 5 tråde og tjekker mod grænser og skive logfil til mas.log
samt skulle der være en alarm som bliv sat til klienten 

**Et forslag eller en ændring fra AI-agenten, som I accepterede:**

public class HQServer {
    private static final int PORT = 5000;
    private static final int WORKER_COUNT = 5;
public static void main(String[] args) {
        ExecutorService workerPool = Executors.newFixedThreadPool(WORKER_COUNT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("HQServer started on port " + PORT);

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                workerPool.submit(() -> handleClient(clientSocket));
            }
**Et forslag eller en ændring fra AI-agenten, som I ændrede eller afviste:**
public HQServer(int port) {
        this(port, Executors.newFixedThreadPool(DEFAULT_WORKER_COUNT));
    }
    

    HQServer(int port, ExecutorService workerPool) {
        if (port < 0 || port > 65_535) {
            throw new IllegalArgumentException("port must be between 0 and 65535");
        }
        this.port = port;
        this.workerPool = Objects.requireNonNull(workerPool, "workerPool must not be null");
    }
**Hvordan testede I, at AI-generede kode virkede?**
unit tested coden for at være sikker på at valideringen virked samt at programet ville give besked på at der var noget der var overskredet 
