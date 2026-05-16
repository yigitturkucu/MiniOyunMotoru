import java.util.ArrayList;
import java.util.List;

class OyunDurumu {
    private static OyunDurumu ornek;

    private int bolum      = 1;
    private int toplamskor = 0;
    private boolean devam  = false;

    private OyunDurumu() {}

    public static OyunDurumu getInstance() {
        if (ornek == null) {
            ornek = new OyunDurumu();
        }
        return ornek;
    }

    public int getBolum()           { return bolum; }
    public void setBolum(int b)     { this.bolum = b; }
    public int getToplamskor()      { return toplamskor; }
    public void skorEkle(int puan)  { toplamskor += puan; }
    public boolean isDevam()        { return devam; }
    public void setDevam(boolean d) { this.devam = d; }
}

class OyunNesnesi {
    String tip;
    String isim;
    int x, y;
    int can;
    int saldiri;
    int hiz;
    boolean hayatta  = true;
    boolean toplandı = false;
    int skor;
    String dusmandavranisi;
    String esyatipi;

    OyunNesnesi(String tip, String isim, int x, int y) {
        this.tip  = tip;
        this.isim = isim;
        this.x    = x;
        this.y    = y;
    }
}

abstract class NesneFactory {
    public abstract OyunNesnesi olustur(String isim, int x, int y);
}

class OyuncuFactory extends NesneFactory {
    @Override
    public OyunNesnesi olustur(String isim, int x, int y) {
        OyunNesnesi o = new OyunNesnesi("oyuncu", isim, x, y);
        o.can     = 100;
        o.saldiri = 10;
        o.hiz     = 5;
        o.skor    = 0;
        System.out.println("[OyuncuFactory] " + isim + " olusturuldu.");
        return o;
    }
}

class DusmanFactory extends NesneFactory {
    private final String davranis;

    DusmanFactory(String davranis) {
        this.davranis = davranis;
    }

    @Override
    public OyunNesnesi olustur(String isim, int x, int y) {
        OyunNesnesi d = new OyunNesnesi("dusman", isim, x, y);
        d.can             = 50;
        d.saldiri         = 8;
        d.hiz             = 3;
        d.dusmandavranisi = davranis;
        System.out.println("[DusmanFactory] " + isim + " (" + davranis + ") olusturuldu.");
        return d;
    }
}

class EsyaFactory extends NesneFactory {
    private final String esyatip;

    EsyaFactory(String esyatip) {
        this.esyatip = esyatip;
    }

    @Override
    public OyunNesnesi olustur(String isim, int x, int y) {
        OyunNesnesi e = new OyunNesnesi("esya", isim, x, y);
        e.can      = 0;
        e.saldiri  = 0;
        e.hiz      = 0;
        e.esyatipi = esyatip;
        System.out.println("[EsyaFactory] " + isim + " (" + esyatip + ") olusturuldu.");
        return e;
    }
}

class DusmanBuilder {
    private String isim;
    private int x, y;
    private int can      = 50;
    private int saldiri  = 8;
    private int hiz      = 3;
    private String davranis = "saldirgan";

    public DusmanBuilder(String isim, int x, int y) {
        this.isim = isim;
        this.x    = x;
        this.y    = y;
    }

    public DusmanBuilder can(int can)         { this.can      = can;     return this; }
    public DusmanBuilder saldiri(int saldiri) { this.saldiri  = saldiri; return this; }
    public DusmanBuilder hiz(int hiz)         { this.hiz      = hiz;     return this; }
    public DusmanBuilder davranis(String d)   { this.davranis = d;       return this; }

    public OyunNesnesi build() {
        OyunNesnesi d = new OyunNesnesi("dusman", isim, x, y);
        d.can             = can;
        d.saldiri         = saldiri;
        d.hiz             = hiz;
        d.dusmandavranisi = davranis;
        System.out.println("[DusmanBuilder] " + isim
                + " (can=" + can + ", saldiri=" + saldiri
                + ", davranis=" + davranis + ") insa edildi.");
        return d;
    }
}

public class OyunMotoru {

    static List<OyunNesnesi> tumnesneler = new ArrayList<>();
    static OyunNesnesi oyuncu;

    static OyunNesnesi ekle(OyunNesnesi n) {
        tumnesneler.add(n);
        if ("oyuncu".equals(n.tip)) oyuncu = n;
        return n;
    }

    public static void hareket(OyunNesnesi nesne, int dx, int dy) {
        if ("oyuncu".equals(nesne.tip)) {
            if (!nesne.hayatta) { System.out.println("Oyuncu oldu, hareket edemez."); return; }
            nesne.x += dx * nesne.hiz;
            nesne.y += dy * nesne.hiz;
            System.out.println(nesne.isim + " konuma gitti: (" + nesne.x + ", " + nesne.y + ")");

        } else if ("dusman".equals(nesne.tip)) {
            if (!nesne.hayatta) return;
            if ("saldirgan".equals(nesne.dusmandavranisi) && oyuncu != null) {
                int yonx = oyuncu.x > nesne.x ? 1 : -1;
                int yony = oyuncu.y > nesne.y ? 1 : -1;
                nesne.x += yonx * nesne.hiz;
                nesne.y += yony * nesne.hiz;
            } else if ("uzaktan".equals(nesne.dusmandavranisi) && oyuncu != null) {
                int mesafe = Math.abs(oyuncu.x - nesne.x) + Math.abs(oyuncu.y - nesne.y);
                if (mesafe < 10) {
                    int yonx = oyuncu.x > nesne.x ? -1 : 1;
                    nesne.x += yonx * nesne.hiz;
                }
            }
        }
    }

    public static void saldir(OyunNesnesi saldiran, OyunNesnesi hedef) {
        OyunDurumu durum = OyunDurumu.getInstance();

        if ("oyuncu".equals(saldiran.tip)) {
            if (!saldiran.hayatta || !hedef.hayatta) return;
            if ("dusman".equals(hedef.tip)) {
                hedef.can -= saldiran.saldiri;
                System.out.println("Oyuncu " + hedef.isim + "'a " + saldiran.saldiri + " hasar verdi.");
                if (hedef.can <= 0) {
                    hedef.hayatta = false;
                    durum.skorEkle(10);
                    System.out.println(hedef.isim + " olduruldu! Skor: " + durum.getToplamskor());
                }
            } else if ("oyuncu".equals(hedef.tip)) {
                System.out.println("Oyuncu kendine saldiramaz.");
            } else {
                System.out.println("Esyaya saldirilmaz.");
            }

        } else if ("dusman".equals(saldiran.tip)) {
            if (!saldiran.hayatta || !hedef.hayatta) return;
            if ("oyuncu".equals(hedef.tip)) {
                int hasar = "uzaktan".equals(saldiran.dusmandavranisi)
                        ? saldiran.saldiri / 2
                        : ("saldirgan".equals(saldiran.dusmandavranisi) ? saldiran.saldiri : 0);
                hedef.can -= hasar;
                System.out.println(saldiran.isim + " oyuncuya " + hasar + " hasar verdi.");
                if (hedef.can <= 0) {
                    hedef.hayatta = false;
                    System.out.println("Oyuncu oldu! Oyun bitti.");
                    durum.setDevam(false);
                }
            }
        } else {
            System.out.println("Esya saldiramaz.");
        }
    }

    public static void esyatopla(OyunNesnesi oyuncunesne, OyunNesnesi esya) {
        OyunDurumu durum = OyunDurumu.getInstance();
        if (!"oyuncu".equals(oyuncunesne.tip)) { System.out.println("Sadece oyuncu esya toplayabilir."); return; }
        if (!"esya".equals(esya.tip))           { System.out.println("Bu bir esya degil.");               return; }
        if (esya.toplandı)                       { System.out.println("Bu esya zaten toplandi.");          return; }

        esya.toplandı = true;
        esya.hayatta  = false;

        switch (esya.esyatipi) {
            case "can_iksiri":
                oyuncunesne.can = Math.min(oyuncunesne.can + 30, 100);
                System.out.println("Can iksiri kullanildi! Can: " + oyuncunesne.can);
                break;
            case "kilic":
                oyuncunesne.saldiri += 5;
                System.out.println("Kilic toplandi! Saldiri gucu: " + oyuncunesne.saldiri);
                break;
            case "altin":
                durum.skorEkle(5);
                System.out.println("Altin toplandi! Skor: " + durum.getToplamskor());
                break;
        }
    }

    public static void oyunuguncelle() {
        OyunDurumu durum = OyunDurumu.getInstance();
        if (!durum.isDevam()) return;

        for (OyunNesnesi nesne : tumnesneler) {
            if (!nesne.hayatta) continue;
            if ("dusman".equals(nesne.tip)) {
                hareket(nesne, 0, 0);
                if (oyuncu != null && oyuncu.hayatta) {
                    int mesafe = Math.abs(oyuncu.x - nesne.x) + Math.abs(oyuncu.y - nesne.y);
                    if (mesafe <= 2) saldir(nesne, oyuncu);
                }
            }
        }

        boolean tumdusmanlaroldu = tumnesneler.stream()
                .noneMatch(n -> "dusman".equals(n.tip) && n.hayatta);
        if (tumdusmanlaroldu) {
            durum.setBolum(durum.getBolum() + 1);
            System.out.println("=== BOLUM " + durum.getBolum() + " ===");
            bolumdusmanlariniolustur(durum.getBolum());
        }
    }

    public static void bolumdusmanlariniolustur(int bolum) {
        if (bolum == 2) {
            ekle(new DusmanFactory("saldirgan").olustur("Goblin_Lider", 20, 20)).can = 80;
            ekle(new DusmanFactory("uzaktan").olustur("Ok_Atan", 30, 10));

        } else if (bolum == 3) {
            ekle(new DusmanBuilder("PATRON", 50, 50)
                    .can(200)
                    .saldiri(25)
                    .hiz(2)
                    .davranis("saldirgan")
                    .build());

        } else if (bolum >= 4) {
            for (int i = 0; i < bolum; i++) {
                ekle(new DusmanBuilder("Dusman_" + i, i * 5, i * 5)
                        .can(50 + bolum * 10)
                        .build());
            }
        }
    }

    public static void oyunubaslat() {
        System.out.println("Oyun basliyor!");
        OyunDurumu.getInstance().setDevam(true);

        ekle(new OyuncuFactory().olustur("Kahraman", 0, 0));

        ekle(new DusmanFactory("saldirgan").olustur("Goblin", 10, 10));
        ekle(new DusmanFactory("pasif").olustur("Slime", 15, 5));

        ekle(new EsyaFactory("can_iksiri").olustur("Can_Iksiri", 5, 5));
        ekle(new EsyaFactory("altin").olustur("Altin", 8, 8));
    }

    public static void durumyazdir() {
        OyunDurumu durum = OyunDurumu.getInstance();
        System.out.println("--- OYUN DURUMU ---");
        System.out.println("Bolum: " + durum.getBolum() + " | Toplam Skor: " + durum.getToplamskor());
        for (OyunNesnesi nesne : tumnesneler) {
            if (!nesne.hayatta) continue;
            switch (nesne.tip) {
                case "oyuncu" -> System.out.println("[OYUNCU] " + nesne.isim
                        + " - Can: " + nesne.can + " | Saldiri: " + nesne.saldiri
                        + " | Konum: (" + nesne.x + "," + nesne.y + ")");
                case "dusman" -> System.out.println("[DUSMAN] " + nesne.isim
                        + " - Can: " + nesne.can + " | Davranis: " + nesne.dusmandavranisi
                        + " | Konum: (" + nesne.x + "," + nesne.y + ")");
                case "esya"   -> System.out.println("[ESYA]   " + nesne.isim
                        + " - Tip: " + nesne.esyatipi
                        + " | Konum: (" + nesne.x + "," + nesne.y + ")");
            }
        }
        System.out.println("-------------------");
    }

    public static void main(String[] args) {
        oyunubaslat();
        durumyazdir();

        oyunuguncelle();

        hareket(oyuncu, 1, 1);

        OyunNesnesi goblin = tumnesneler.get(1);
        for (int i = 0; i < 5; i++) saldir(oyuncu, goblin);

        OyunNesnesi iksir = tumnesneler.get(3);
        esyatopla(oyuncu, iksir);

        oyunuguncelle();
        durumyazdir();
    }
}