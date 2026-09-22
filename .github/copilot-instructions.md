# Mission Marsbase Monitorering - Java

Dette er en simpel Java-konsolapplikation. Den er designet til at simulere en mission på Mars, 
hvor forskellige sensorer sender data til HQServer, som derefter håndterer og behandler disse data.
HQ logger alt og sender en "ALARM" hvis noget er farligt for kolonisterne.

## Teknologi
- Brug Java 24.
- Brug ikke database. Med mindre vi specifikt beder om dette.
- Brug ikke Spring Boot eller andre frameworks.
- Hold løsningen enkel og forståelig.
- Brug Junit 5.10.2 til unittests.

## GitHub workflow
Når du arbejder med en udviklingsopgave:
1. Brug et rigtigt GitHub Issue som kilde til opgaven.
2. Hent GitHub Issue gennem GitHub MCP.
3. Brug ikke README eller lokale markdown-filer som erstatning for et GitHub Issue.
4. Arbejd kun med det Issue, brugeren har valgt eller som er assigned til brugeren.
5. Læs hele Issue og alle acceptkriterier før du planlægger.
6. Fortæl altid Issue-nummer og titel, før du foreslår en plan.
7. Lav en kort implementeringsplan før kode ændres.
8. Opret en seperate branch til Issue.
9. Implementer kun det valgte Issue.
10. Kør relevante test efter implementering.
11. Udfør kun unittests, når du bliver bedt om det. Ellers skal der testes manuelt.
12. Kontroller alle acceptkriterier.
13. Referer Issue-nummeret i pull requesten.
14. Vent på review og godkendelse før merge.
15. Merge ikke uden menneskelig godkendelse.
16. Luk ikke selv et Issue uden menneskelig godkendelse.

## Kvalitet
AI-genereret kode er ikke automatisk korrekt.

Koden skal kunne forklares, testes og reviewes af udvikleren.