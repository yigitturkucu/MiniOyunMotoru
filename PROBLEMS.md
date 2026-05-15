# Başlangıç Kodunu Anlama (Faz 0)

## Kendi Tespit Ettiğim Sorunlar
1. **Her Şeyi Tek Sınıfa Yığmak:** `OyunMotoru` sınıfı resmen çorba gibi oldu. Karakterin yaratılması, hareket etmesi, hasar hesaplaması ve level atlama mantığının hepsi tek bir yerde. Kodda bir hata çıksa neresinden kaynaklandığını bulmak eziyet.
2. **Bitmek Bilmeyen If-Else Zincirleri:** `hareket`, `saldir` veya `esyatopla` metotlarının içi tamamen `if(tip == "oyuncu") else if(tip == "dusman")` şeklinde gidiyor. Oyuna yeni bir düşman tipi veya yeni bir iksir eklemeye kalksam her metodun içine yeni bir `else if` bloğu yazmak zorundayım.
3. **Saçma Değişken Kalabalığı:** Bütün nesneleri tek bir sınıftan ürettiğim için, örneğin bir "altın" eşyası sırf aynı sınıftayız diye hafızada kendi "canı" veya "saldırı gücü" varmış gibi değişkenler tutuyor. Verimsiz bir yapı oldu.
4. **Nesnelerin Yaratım Sürecindeki Karmaşa:** Constructor (yapıcı metot) içi tamamen spagettiye bağladı. Oyuncu, düşman ve eşya yaratılırken hepsi aynı constructor'a girip `if-else` ile kendi değerlerini arıyor. Bu hiç esnek bir nesne üretme mantığı değil.
5. **Yapay Zeka Mantığının İlkel Olması:** Düşmanların "saldirgan" veya "uzaktan" gibi davranışlarını dümdüz metin (String) olarak tutup `if` ile kontrol ediyorum. İleride farklı tepkiler veren karmaşık bir boss eklemek istesem bu düz mantıkla altından kalkamam.

---

## Yapay Zeka (AI) Karşılaştırması (ChatGPT İle Karşılaştırma)

**AI'a sorduğum prompt:** *"Bu kodda hangi tasarım sorunlarını görüyorsun? Hangi tasarım örüntüleri bu sorunları çözebilir? Her sorun için kısa bir açıklama yaz."*

**AI'ın Yanıt Özeti:**
Kod incelendiğinde, tüm oyun mekaniklerinin tek bir sınıf içinde toplandığı ve bu nedenle yapının “God Object” (Tanrı Nesne) anti-patternine dönüştüğü görülmektedir. Oyuncu, düşman, eşya, savaş sistemi ve oyun yönetimi aynı sınıf tarafından kontrol edildiği için kodun bakımının ve geliştirilmesinin zorlaşacağı belirtilmiştir.
Yapay zeka asistanı, bu sorunu çözmek için sınıfların sorumluluklarına göre ayrılması gerektiğini önerdi. Özellikle nesne oluşturma sürecindeki karmaşık if-else yapılarının azaltılması amacıyla Factory Method tasarım örüntüsünün kullanılabileceği ifade edildi. Ayrıca düşman davranışlarının (saldırgan, pasif, uzaktan vb.) doğrudan string kontrolüyle yönetilmesi yerine, davranışların ayrı sınıflar halinde tanımlanmasını sağlayan Strategy Pattern önerildi.
Bunun yanında, sistemde yoğun şekilde kullanılan static global değişkenlerin bağımlılığı artırdığı ve test edilebilirliği zorlaştırdığı vurgulandı. Genel olarak kodun daha modüler, genişletilebilir ve sürdürülebilir hale getirilmesi için nesne yönelimli prensiplere uygun bir mimariye geçilmesi gerektiği sonucuna varıldı.

**Karşılaştırma :**
Kendi tespitlerim ile yapay zekanın analizi büyük ölçüde örtüşmektedir. Benim "her şeyi tek sınıfa yığmak" diyerek hissettiğim pratik zorluğu, AI "God Object" anti-paterni olarak teknik bir kavrama oturtmuştur. Aynı şekilde, nesne yaratımındaki ve davranış kontrolündeki bitmek bilmeyen if-else zincirlerinden duyduğum rahatsızlığı AI da tespit etmiş; bu spagetti yapıya spesifik çözümler (**Factory** ve **Strategy** örüntüleri) sunmuştur. 
Aramızdaki temel fark bakış açısındadır: Ben daha çok "yeni kod ekleme zorluğu" ve "hafızadaki gereksiz değişken kalabalığı" gibi geliştiriciyi anlık olarak yoran sorunlara odaklanırken; AI, *static global değişkenlerin test edilebilirliği azaltması* gibi daha geniş çaplı, mimari ve uzun vadeli sorunlara da dikkat çekmiştir.
