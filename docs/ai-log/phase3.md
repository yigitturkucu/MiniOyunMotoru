# Faz 3: AI Pair Programming ve Behavioral Analiz Logu

### 1. Oturum Özeti: Ne Tartıştık, Nasıl İlerledik?
Bu fazda AI ile yaklaşık 40 dakikalık bir "Pair Programming" oturumu gerçekleştirdik. Ana tartışma konumuz, projeye eklenecek yeni davranışların **Açık/Kapalı Prensibini (OCP)** nasıl ihlal etmeden entegre edileceğiydi. 
Düşmanların farklı saldırı tiplerini yönetmek için **Strategy Pattern** kullanılmasına, oyundaki skor değişimlerinin başarı sistemini tetiklemesi için ise **Observer Pattern** kullanılmasına karar verdik. AI bana arayüzlerin (interface) taslaklarını sundu, ben de bu arayüzleri kendi oyun motoru mantığıma göre implement ettim.

### 2. AI Sizi Nerede Yanılttı? (Kritik Analiz)
AI ile çalışırken iki noktada kritik mimari hatalar fark ettim ve müdahale ettim:
* **Strategy Pattern Hatası:** AI, düşman davranışlarını (Strategy) belirlerken arka planda gizli bir `enum` ve `switch-case` yapısı kullanmayı teklif etti. Bu durum doğrudan OCP'yi (Open/Closed Principle) ihlal ediyordu çünkü yeni bir strateji eklediğimde switch-case bloğunu da değiştirmem gerekecekti. AI'ı uyararak bu yapıyı tamamen Interface üzerinden çalışan *Polymorphism* (Çok biçimlilik) tabanlı bir Strategy örüntüsüne çevirdim.
* **Observer Pattern Hatası:** AI, Observer örüntüsü için araya karmaşık bir "Event Manager" (Mediator benzeri) sınıfı koymayı önerdi. Projenin mevcut ölçeğinde bu aşırı mühendislik (over-engineering) olacaktı. Bu yüzden aracı sınıfı reddedip, doğrudan Subject ve Observer arayüzlerini kullanarak daha sade ve amaca yönelik bir yapı kurdum.

### 3. AI Olmadan Bu Faz Ne Kadar Sürerdi?
Eğer tasarım örüntülerinin teorik araştırmasını, UML diyagramlarının `mermaid` formatında kodlanmasını ve boilerplate (tekrar eden) arayüz kodlarının yazımını tamamen tek başıma yapsaydım bu faz muhtemelen **3 ile 4 saat** arasında vaktimi alırdı. AI ile pair programming yapmak, beyin fırtınası sürecini hızlandırdığı için bu süreyi **1.5 saate** kadar indirdi.