Všetci používatelia používajú rovnaké heslo:

**Password123!**

Demo účty:

Customer (Zákazník)	zakaznik@test.sk

Cook (Kuchyňa)	kuchar@test.sk

Courier (Delivery)	kurier@test.sk

Admin	admin@test.sk


**Roly a správanie**

**Customer (Zákazník)**
- Prehliadanie menu
- Pridávanie pízz a ingrediencií do košíka
- Vytvorenie objednávky
- Zobrazenie iba vlastných objednávok
- Zrušenie objednávky iba v stave Čakajúca (PENDING)
  
**Cook (Kuchyňa)**
- Vidí objednávky v stavoch: Čakajúca, V príprave
- Vidí položky objednávky, extra ingrediencie a poznámku zákazníka
- Mení stav objednávky: Čakajúca → V príprave, V príprave → Pripravená
  
**Courier (Delivery)**
- Vidí objednávky v stave Pripravená
- Mení stav objednávky: Pripravená → Doručuje sa, Doručuje sa → Doručená
  
**Admin**
- Správa pízz
- Správa ingrediencií
- Administrátorský prístup

  
**Stavy objednávok**

- PENDING (Čakajúca) — objednávka vytvorená
- PREPARING (V príprave) — objednávka sa pripravuje
- READY (Pripravená) — objednávka je pripravená
- DELIVERING (Doručuje sa) — objednávka sa doručuje
- DELIVERED (Doručená) — objednávka bola doručená
- CANCELLED (Zrušená) — zrušená zákazníkom (iba zo stavu PENDING)
  
**Obmedzenia prístupu**
- Zákazník nemôže zrušiť objednávku, ak je už v príprave, pripravená alebo doručovaná
- Používateľ nevidí administrátorské rozhrania
- Personál nemá prístup k používateľským stránkam
- Prístup k stránkam je riadený rolami
