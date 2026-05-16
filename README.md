# Mini Oyun Motoru

### 1. Projenin Ne Yaptığı
Bu proje, Java kullanılarak geliştirilmiş terminal tabanlı bir mini oyun motorudur. Oyuncu, düşman ve eşya gibi nesnelerin yaratılması, hareket ettirilmesi, savaş mekanikleri ve oyun içi olayların yönetilmesi gibi temel oyun döngülerini içerir. Sistem, spagetti kod (if-else yığınları) yerine profesyonel **Yazılım Tasarım Örüntüleri (Design Patterns)** kullanılarak, genişletilebilir (Açık/Kapalı Prensibine - OCP uyumlu) ve modüler bir mimaride tasarlanmıştır.

### 2. Kullanılan Tüm Örüntüler ve Kısa Açıklamaları
Projenin geliştirme aşamalarında toplam 3 farklı kategoride 7 tasarım örüntüsü uygulanmıştır:

**Creational (Yaratımsal) Örüntüler:**
* **Singleton:** Oyun durumu (`oyundurumu`) ve olay yöneticisinin (`oyunolayyayinci`) tek bir instance üzerinden yönetilmesi için.
* **Factory Method:** Oyuncu, düşman ve eşya nesnelerinin ortak bir arayüzden (`nesnefactory`) türetilmesi için.
* **Builder:** Çok parametreli karmaşık düşman nesnelerinin (örn: Boss) adım adım ve okunabilir şekilde oluşturulması için.

**Structural (Yapısal) Örüntüler:**
* **Facade:** Savaş, hareket ve eşya toplama gibi karmaşık alt sistemleri tek bir basit arayüz (`oyunfacade`) arkasında gizlemek için.
* **Decorator:** Nesnelere çalışma zamanında dinamik olarak zehir veya kalkan gibi yeni özellikler ekleyebilmek için.

**Behavioral (Davranışsal) Örüntüler:**
* **Strategy:** Düşman ve oyuncu saldırı davranışlarını (yakın, alan, kritik) polimorfizm ile çalışma zamanında değiştirebilmek için.
* **Observer:** Oyundaki ölüm, bölüm atlama gibi olayları merkezi bir sistemden yayınlayıp, bağımsız dinleyicileri (log, sayaç) tetiklemek için.

### 3. Mimari Diyagram (Görsel)
Projedeki tüm örüntülerin detaylı UML diyagramları ve aralarındaki ilişkiler `PATTERNS.md` dosyasında dokümante edilmiştir.
> **[UML Diyagramlarını ve Mimariyi Görmek İçin Tıklayın (PATTERNS.md)](./PATTERNS.md)**

### 4. Nasıl Çalıştırılır?
Projeyi bilgisayarınızda derleyip çalıştırmak için terminalde şu komutları sırasıyla çalıştırmanız yeterlidir:

```bash
# Kodları 'bin' klasörüne derlemek için:
mkdir -p bin
javac -d bin src/OyunMotoru.java

# Oyunu çalıştırmak için:
java -cp bin OyunMotoru