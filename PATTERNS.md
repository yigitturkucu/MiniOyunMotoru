# Uygulanan Tasarım Örüntüleri (Faz 1)

### 1. Singleton (`oyundurumu`)
* **Nerede:** Oyunun bölüm, toplam skor ve devam edip etmeme durumunun tutulduğu yerde uygulandı.
* **Neden:** Tüm nesnelerin tek bir merkezi oyun durumuna erişmesi gerekiyordu.
* **Ne Kazandırdı:** Global değişken karmaşasını önledi, bellekte ikinci bir durum nesnesi üretilmesini engelledi.

### 2. Factory Method (`nesnefactory`)
* **Nerede:** Oyuncu, düşman ve eşya nesnelerinin ilk üretim aşamasında uygulandı.
* **Neden:** `OyunMotoru` sınıfını doğrudan `new` anahtar kelimesiyle nesne yaratma yükünden kurtarmak için.
* **Ne Kazandırdı:** Kodun Açık/Kapalı Prensibine (Open/Closed Principle) uymasını sağladı. Yeni bir nesne tipi eklendiğinde ana motor kodunun değişmesine gerek kalmadı.

### 3. Builder (`dusmanbuilder`)
* **Nerede:** 3. Bölümdeki `PATRON` nesnesi ve sonraki bölümlerdeki çok parametreli düşmanların üretiminde uygulandı.
* **Neden:** Can, saldırı, hız gibi çok sayıda isteğe bağlı parametreye sahip nesneleri esnek üretmek için.
* **Ne Kazandırdı:** Yapıcı metot (constructor) kirliliğini önledi ve koda akıcı bir okunabilirlik kazandırdı.

### UML Sınıf Diyagramı (Önce / Sonra)

**Önceki Yapı (God Class):**
```mermaid
classDiagram
    class OyunMotoru {
        -int bolum
        -int toplamSkor
        +dusmanYarat()
        +oyuncuYarat()
        +oyunuBaslat()
    }
    note for OyunMotoru "Tüm durumlar ve nesne<br>yaratım if-else blokları<br>tek sınıfta (God Class)."
```

**Sonraki Yapı (Tasarım Örüntüleri ile):**
```mermaid
classDiagram
    class OyunMotoru {
        +oyunuBaslat()
    }
    class OyunDurumu {
        <<Singleton>>
        -static ornek
        +getInstance()
    }
    class NesneFactory {
        <<Factory Method>>
        +nesneUret(tip)
    }
    class DusmanBuilder {
        <<Builder>>
        +setCan()
        +setHasar()
        +build()
    }
    
    OyunMotoru ..> OyunDurumu : Duruma erişir
    OyunMotoru ..> NesneFactory : Nesne talep eder
    NesneFactory ..> DusmanBuilder : Karmaşık düşmanları üretir
```

---

# Uygulanan Tasarım Örüntüleri (Faz 2)

### 1. Facade (`oyunfacade`)
* **Nerede:** Oyunun başlatılması, karakter hareketleri, savaş tetiklemeleri ve eşya toplama gibi tüm alt sistem işlemlerinin merkezi olarak yönetildiği arayüzde uygulandı.
* **Neden:** `OyunMotoru` sınıfındaki karmaşıklığı azaltmak (Adapter yerine Facade seçildi çünkü uyumsuz arayüzleri birleştirmiyoruz, karmaşık bir yapıyı basitleştiriyoruz).
* **Ne Kazandırdı:** Ana metodun oyun nesnelerinin iç yapısını bilmesine gerek kalmadı. Temiz ve basit bir kullanım sağlandı.

### 2. Decorator (`oyunnesnesidecorator`, `zehirdecorator`, `kalkandecorator`)
* **Nerede:** Oyun nesnelerinin özelliklerini (zehirli saldırı, kalkan koruması vb.) dinamik olarak değiştirmek için uygulandı.
* **Neden:** Özellikleri nesnelere alt sınıflar açarak kalıcı olarak eklemek yerine, çalışma zamanında (runtime) esnek bir şekilde giydirip çıkarabilmek için.
* **Ne Kazandırdı:** Sınıf patlamasını önledi ve Açık/Kapalı Prensibine (Open/Closed Principle) tam uyum sağlandı.

### Faz 2 Mimari Diyagram (UML)

```mermaid
classDiagram
    class oyunfacade {
        +baslat()
        +oyuncuhareket(dx, dy)
        +saldir(hedefisim)
        +dusmanSaldir(saldıranisim)
        +esyatoplat(esyaisim)
    }
    
    class oyunnesnesidecorator {
        <<Decorator>>
        #oyunnesnesi sarilan
        +saldir(hedef, temelhasar)
        +hasaral(gelenhasar)
    }
    
    class zehirdecorator {
        -int zehirhasari
        +saldir(hedef, temelhasar)
    }
    
    class kalkandecorator {
        +hasaral(gelenhasar)
    }

    oyunnesnesidecorator <|-- zehirdecorator
    oyunnesnesidecorator <|-- kalkandecorator
    oyunfacade --> oyunnesnesidecorator : Tetikler
```

---

# Uygulanan Tasarım Örüntüleri (Faz 3)

### 1. Strategy Pattern (`saldiriStratejisi`)
* **Nerede:** Oyuncu ve düşman nesnelerinin saldırı mekanizmalarında uygulandı. `yakinSaldiriStratejisi`, `alanSaldiriStratejisi` ve `kritikSaldiriStratejisi` sınıfları türetildi.
* **Neden:** Yeni bir saldırı veya davranış tipi eklendiğinde mevcut kodları (if-else veya switch-case yapılarını) değiştirmeden, Açık/Kapalı Prensibine (OCP) tam uyumlu şekilde sisteme yeni yetenekler kazandırabilmek için.
* **Ne Kazandırdı:** Oyun motoru genişletilebilir bir yapıya kavuştu. Sistem mevcut kod tabanını kırmadan yeni stratejilerle büyütülebilir hale geldi.

### 2. Observer Pattern (`oyunolayyayinci`, `oyunolayidinleyici`)
* **Nerede:** Oyundaki kritik olayların (Ölüm, Bölüm Atlama, Nesne Oluşturulma, Strateji Değişimi) sisteme duyurulmasında ve dinlenmesinde uygulandı. `konsollogdinleyici`, `olumdinleyici` ve `bolumdinleyici` sınıfları bu olayları dinlemektedir.
* **Neden:** Nesneler arasındaki bağımlılığı gevşetmek (Loose Coupling) ve bir nesnenin durumundaki değişikliği diğer tüm bağımlı nesnelere anında ve otomatik olarak bildirmek için.
* **Ne Kazandırdı:** Başarı sistemleri, loglama mekanizmaları ve sayaçlar oyun motorunun ana iş mantığına sıkı sıkıya bağlanmadan, tamamen bağımsız modüller olarak sisteme entegre edilebildi.

### Faz 3 Mimari Diyagram (UML)

```mermaid
classDiagram
    class saldiriStratejisi {
        <<Interface>>
        +saldir(saldiran, hedef)
    }
    class yakinSaldiriStratejisi {
        +saldir(saldiran, hedef)
    }
    class alanSaldiriStratejisi {
        +saldir(saldiran, hedef)
    }
    class kritikSaldiriStratejisi {
        +saldir(saldiran, hedef)
    }
    saldiriStratejisi <|.. yakinSaldiriStratejisi
    saldiriStratejisi <|.. alanSaldiriStratejisi
    saldiriStratejisi <|.. kritikSaldiriStratejisi

    class oyunolayidinleyici {
        <<Interface>>
        +olaygeldi(olaytipi, mesaj)
    }
    class konsollogdinleyici {
        +olaygeldi(olaytipi, mesaj)
    }
    class olumdinleyici {
        +olaygeldi(olaytipi, mesaj)
    }
    class bolumdinleyici {
        +olaygeldi(olaytipi, mesaj)
    }
    oyunolayidinleyici <|.. konsollogdinleyici
    oyunolayidinleyici <|.. olumdinleyici
    oyunolayidinleyici <|.. bolumdinleyici

    class oyunolayyayinci {
        <<Singleton>>
        -static oyunolayyayinci ornek
        -List~oyunolayidinleyici~ dinleyiciler
        +getinstance()
        +dinleyiciekle(d)
        +yayinla(olaytipi, mesaj)
    }
    oyunolayyayinci --> oyunolayidinleyici : Bildirim Yapar
```