import java.util.ArrayList;
import java.util.List;

interface oyunolayidinleyici
{
    void olaygeldi(String olaytipi, String mesaj);
}

class oyunolayyayinci
{
    private static oyunolayyayinci ornek;
    private final List<oyunolayidinleyici> dinleyiciler = new ArrayList<>();

    private oyunolayyayinci() {}

    public static oyunolayyayinci getinstance()
    {
        if (ornek == null) ornek = new oyunolayyayinci();
        return ornek;
    }

    public void dinleyiciekle(oyunolayidinleyici d) { dinleyiciler.add(d); }
    public void dinleyicicikart(oyunolayidinleyici d) { dinleyiciler.remove(d); }

    public void yayinla(String olaytipi, String mesaj)
    {
        for (oyunolayidinleyici d : dinleyiciler)
            d.olaygeldi(olaytipi, mesaj);
    }
}

class konsollogdinleyici implements oyunolayidinleyici
{
    @Override
    public void olaygeldi(String olaytipi, String mesaj)
    {
        System.out.println("[LOG|" + olaytipi + "] " + mesaj);
    }
}

class olumdinleyici implements oyunolayidinleyici
{
    private int oldurme = 0;

    @Override
    public void olaygeldi(String olaytipi, String mesaj)
    {
        if ("OLUM".equals(olaytipi))
        {
            oldurme++;
            System.out.println("[OLUM_SAYACI] Toplam oldurme: " + oldurme + " -> " + mesaj);
        }
    }

    public int getoldurme() { return oldurme; }
}

class bolumdinleyici implements oyunolayidinleyici
{
    @Override
    public void olaygeldi(String olaytipi, String mesaj)
    {
        if ("BOLUM".equals(olaytipi))
            System.out.println("[BOLUM_ATLANDI] " + mesaj + " -> Yeni bolum basliyor!");
    }
}

interface saldiriStratejisi
{
    void saldir(oyunnesnesi saldiran, oyunnesnesi hedef);
}

class yakinSaldiriStratejisi implements saldiriStratejisi
{
    @Override
    public void saldir(oyunnesnesi saldiran, oyunnesnesi hedef)
    {
        int hasar = saldiran.saldiri;
        hedef.can -= hasar;
        System.out.println("[STRATEJI:YAKIN] " + saldiran.isim + " -> " + hedef.isim
                + " : " + hasar + " hasar (kalan: " + hedef.can + ")");
        olayKontrol.kontrol(hedef);
    }
}

class alanSaldiriStratejisi implements saldiriStratejisi
{
    private final List<oyunnesnesi> tumnesneler;

    alanSaldiriStratejisi(List<oyunnesnesi> tumnesneler)
    {
        this.tumnesneler = tumnesneler;
    }

    @Override
    public void saldir(oyunnesnesi saldiran, oyunnesnesi hedef)
    {
        int temelhasar = saldiran.saldiri / 2;
        System.out.println("[STRATEJI:ALAN] " + saldiran.isim
                + " alan saldirisi! Her dusmaxa " + temelhasar + " hasar.");
        for (oyunnesnesi n : tumnesneler)
        {
            if ("dusman".equals(n.tip) && n.hayatta)
            {
                n.can -= temelhasar;
                System.out.println("   -> " + n.isim + " hasar aldi (kalan: " + n.can + ")");
                olayKontrol.kontrol(n);
            }
        }
    }
}

class kritikSaldiriStratejisi implements saldiriStratejisi
{
    @Override
    public void saldir(oyunnesnesi saldiran, oyunnesnesi hedef)
    {
        int hasar = saldiran.saldiri * 3;
        hedef.can -= hasar;
        System.out.println("[STRATEJI:KRITIK] " + saldiran.isim + " KRITIK VURUS -> "
                + hedef.isim + " : " + hasar + " hasar (kalan: " + hedef.can + ")");
        olayKontrol.kontrol(hedef);
    }
}

class olayKontrol
{

    static void kontrol(oyunnesnesi n)
    {
        if (n.can <= 0 && n.hayatta)
        {
            n.hayatta = false;
            oyundurumu.getinstance().skobekle(10);
            oyunolayyayinci.getinstance().yayinla("OLUM",
                    n.isim + " hayatını kaybetti! Skor: "
                    + oyundurumu.getinstance().gettoplamskor());
        }
    }
}

class oyundurumu
{
    private static oyundurumu ornek;

    private int bolum      = 1;
    private int toplamskor = 0;
    private boolean devam  = false;

    private oyundurumu() {}

    public static oyundurumu getinstance()
    {
        if (ornek == null) ornek = new oyundurumu();
        return ornek;
    }

    public int getbolum()        { return bolum; }
    public void setbolum(int b)  { this.bolum = b; }
    public int gettoplamskor()   { return toplamskor; }
    public void skobekle(int p)  { toplamskor += p; }
    public boolean isdevam()     { return devam; }
    public void setdevam(boolean d) { this.devam = d; }
}

class oyunnesnesi
{
    String tip;
    String isim;
    int x, y;
    int can;
    int saldiri;
    int hiz;
    boolean hayatta  = true;
    boolean toplandi = false;
    int skor;
    String dusmandavranisi;
    String esyatipi;

    oyunnesnesi(String tip, String isim, int x, int y)
    {
        this.tip  = tip;
        this.isim = isim;
        this.x    = x;
        this.y    = y;
    }
}

abstract class nesnefactory
{
    public abstract oyunnesnesi olustur(String isim, int x, int y);
}

class oyuncufactory extends nesnefactory
{
    @Override
    public oyunnesnesi olustur(String isim, int x, int y)
    {
        oyunnesnesi o = new oyunnesnesi("oyuncu", isim, x, y);
        o.can = 100; o.saldiri = 10; o.hiz = 5; o.skor = 0;
        System.out.println("[oyuncufactory] " + isim + " olusturuldu.");
        oyunolayyayinci.getinstance().yayinla("NESNE_OLUSTURULDU",
                isim + " (oyuncu) olusturuldu.");
        return o;
    }
}

class dusmanfactory extends nesnefactory
{
    private final String davranis;
    dusmanfactory(String davranis) { this.davranis = davranis; }

    @Override
    public oyunnesnesi olustur(String isim, int x, int y)
    {
        oyunnesnesi d = new oyunnesnesi("dusman", isim, x, y);
        d.can = 50; d.saldiri = 8; d.hiz = 3; d.dusmandavranisi = davranis;
        System.out.println("[dusmanfactory] " + isim + " (" + davranis + ") olusturuldu.");
        oyunolayyayinci.getinstance().yayinla("NESNE_OLUSTURULDU",
                isim + " (dusman/" + davranis + ") olusturuldu.");
        return d;
    }
}

class esyafactory extends nesnefactory
{
    private final String esyatip;
    esyafactory(String esyatip) { this.esyatip = esyatip; }

    @Override
    public oyunnesnesi olustur(String isim, int x, int y)
    {
        oyunnesnesi e = new oyunnesnesi("esya", isim, x, y);
        e.can = 0; e.saldiri = 0; e.hiz = 0; e.esyatipi = esyatip;
        System.out.println("[esyafactory] " + isim + " (" + esyatip + ") olusturuldu.");
        return e;
    }
}

class dusmanbuilder
{
    private String isim;
    private int x, y;
    private int can = 50, saldiri = 8, hiz = 3;
    private String davranis = "saldirgan";

    public dusmanbuilder(String isim, int x, int y) { this.isim = isim; this.x = x; this.y = y; }
    public dusmanbuilder can(int c)       { this.can = c; return this; }
    public dusmanbuilder saldiri(int s)   { this.saldiri = s; return this; }
    public dusmanbuilder hiz(int h)       { this.hiz = h; return this; }
    public dusmanbuilder davranis(String d) { this.davranis = d; return this; }

    public oyunnesnesi build()
    {
        oyunnesnesi d = new oyunnesnesi("dusman", isim, x, y);
        d.can = can; d.saldiri = saldiri; d.hiz = hiz; d.dusmandavranisi = davranis;
        System.out.println("[dusmanbuilder] " + isim
                + " (can=" + can + ", saldiri=" + saldiri + ", davranis=" + davranis + ") insa edildi.");
        return d;
    }
}

abstract class oyunnesnesidecorator
{
    protected final oyunnesnesi sarilan;
    oyunnesnesidecorator(oyunnesnesi sarilan) { this.sarilan = sarilan; }

    public void saldir(oyunnesnesi hedef, int temelhasar)
    {
        temelhasaruygula(hedef, temelhasar);
    }

    public int hasaral(int gelenhasar) { return gelenhasar; }

    protected void temelhasaruygula(oyunnesnesi hedef, int hasar)
    {
        hedef.can -= hasar;
        System.out.println("[decorator] " + sarilan.isim + " -> " + hedef.isim
                + " temel hasar: " + hasar + "  (kalan can: " + hedef.can + ")");
        if (hedef.can <= 0)
        {
            hedef.hayatta = false;
            System.out.println("[decorator] " + hedef.isim + " olduruldu!");
            oyundurumu.getinstance().skobekle(10);
            oyunolayyayinci.getinstance().yayinla("OLUM",
                    hedef.isim + " dekoratör hasarından öldü.");
        }
    }

    public oyunnesnesi getnesne() { return sarilan; }
}

class zehirdecorator extends oyunnesnesidecorator
{
    private final int zehirhasari;
    zehirdecorator(oyunnesnesi sarilan, int zehirhasari)
    {
        super(sarilan);
        this.zehirhasari = zehirhasari;
        System.out.println("[zehirdecorator] " + sarilan.isim
                + " zehirle kaplaniyor (+" + zehirhasari + " zehir hasari).");
    }

    @Override
    public void saldir(oyunnesnesi hedef, int temelhasar)
    {
        temelhasaruygula(hedef, temelhasar);
        if (hedef.hayatta)
        {
            hedef.can -= zehirhasari;
            System.out.println("[zehirdecorator] " + hedef.isim
                    + " zehirlendi! +" + zehirhasari + " zehir hasari"
                    + "  (kalan can: " + hedef.can + ")");
            if (hedef.can <= 0)
            {
                hedef.hayatta = false;
                System.out.println("[zehirdecorator] " + hedef.isim + " zehirden oldu!");
                oyundurumu.getinstance().skobekle(10);
                oyunolayyayinci.getinstance().yayinla("OLUM",
                        hedef.isim + " zehirden hayatını kaybetti.");
            }
        }
    }
}

class kalkandecorator extends oyunnesnesidecorator
{
    kalkandecorator(oyunnesnesi sarilan)
    {
        super(sarilan);
        System.out.println("[kalkandecorator] " + sarilan.isim
                + " kalkanla donatiliyor (hasar %50 azalir).");
    }

    @Override
    public int hasaral(int gelenhasar)
    {
        int azaltilmis = gelenhasar / 2;
        System.out.println("[kalkandecorator] " + sarilan.isim
                + " kalkani " + gelenhasar + " hasari " + azaltilmis + "'e indirdi.");
        return azaltilmis;
    }
}

class oyunfacade
{
    private final List<oyunnesnesi> tumnesneler = new ArrayList<>();
    private oyunnesnesi oyuncu;

    private final java.util.Map<String, oyunnesnesidecorator> decoratorlar
            = new java.util.HashMap<>();

    private saldiriStratejisi aktifstrateji = new yakinSaldiriStratejisi();

    
    public void stratejidegistir(saldiriStratejisi yenistrateji)
    {
        this.aktifstrateji = yenistrateji;
        System.out.println("[facade] Saldiri stratejisi degistirildi: "
                + yenistrateji.getClass().getSimpleName());
        oyunolayyayinci.getinstance().yayinla("STRATEJI",
                "Strateji degisti: " + yenistrateji.getClass().getSimpleName());
    }

    public void baslat()
    {
        System.out.println("\n[facade] Oyun basliyor...");
        oyundurumu.getinstance().setdevam(true);
        oyunolayyayinci.getinstance().yayinla("OYUN", "Oyun basladi!");

        oyuncu = ekle(new oyuncufactory().olustur("Kahraman", 0, 0));
        ekle(new dusmanfactory("saldirgan").olustur("Goblin", 10, 10));
        ekle(new dusmanfactory("pasif").olustur("Slime", 15, 5));
        ekle(new esyafactory("can_iksiri").olustur("Can_Iksiri", 5, 5));
        ekle(new esyafactory("altin").olustur("Altin", 8, 8));
    }

    public void oyuncuhareket(int dx, int dy)
    {
        if (oyuncu == null || !oyuncu.hayatta)
        {
            System.out.println("[facade] Oyuncu hareket edemez.");
            return;
        }
        oyuncu.x += dx * oyuncu.hiz;
        oyuncu.y += dy * oyuncu.hiz;
        System.out.println("[facade] " + oyuncu.isim
                + " konuma gitti: (" + oyuncu.x + ", " + oyuncu.y + ")");
        oyunolayyayinci.getinstance().yayinla("HAREKET",
                oyuncu.isim + " -> (" + oyuncu.x + "," + oyuncu.y + ")");
    }

    
    public void saldir(String hedefisim)
    {
        oyunnesnesi hedef = nesneara(hedefisim);
        if (hedef == null || !hedef.hayatta)
        {
            System.out.println("[facade] Hedef bulunamadi veya hayatta degil: " + hedefisim);
            return;
        }
        if (!oyuncu.hayatta)
        {
            System.out.println("[facade] Oyuncu olu, saldiramaz.");
            return;
        }

        oyunolayyayinci.getinstance().yayinla("SALDIRI",
                oyuncu.isim + " -> " + hedef.isim + " saldirdi.");

        oyunnesnesidecorator dec = decoratorlar.get(hedefisim);
        if (dec != null)
        {
            dec.saldir(hedef, oyuncu.saldiri);
        }
        else
        {

            aktifstrateji.saldir(oyuncu, hedef);
        }

        if (!hedef.hayatta)
        {
            oyunolayyayinci.getinstance().yayinla("OLUM",
                    hedef.isim + " olduruldu! Skor: " + oyundurumu.getinstance().gettoplamskor());
        }
    }

    
    public void alanSaldirisi()
    {
        if (!oyuncu.hayatta) { System.out.println("[facade] Oyuncu olu."); return; }
        saldiriStratejisi alan = new alanSaldiriStratejisi(tumnesneler);
        System.out.println("\n[facade] ALAN SALDIRISI!");
        alan.saldir(oyuncu, null);
        oyunolayyayinci.getinstance().yayinla("ALAN_SALDIRI",
                oyuncu.isim + " alan saldirisi yapti!");
    }

    public void dusmanSaldir(String saldıranisim)
    {
        oyunnesnesi saldiran = nesneara(saldıranisim);
        if (saldiran == null || !saldiran.hayatta || !"dusman".equals(saldiran.tip)) return;
        if (!oyuncu.hayatta) return;

        int hasar = "uzaktan".equals(saldiran.dusmandavranisi)
                ? saldiran.saldiri / 2
                : ("saldirgan".equals(saldiran.dusmandavranisi) ? saldiran.saldiri : 0);

        oyunnesnesidecorator oyuncudec = decoratorlar.get(oyuncu.isim);
        if (oyuncudec != null) hasar = oyuncudec.hasaral(hasar);

        oyuncu.can -= hasar;
        System.out.println("[facade] " + saldiran.isim + " oyuncuya " + hasar + " hasar verdi."
                + " (oyuncu can: " + oyuncu.can + ")");
        oyunolayyayinci.getinstance().yayinla("DUSMAN_SALDIRI",
                saldiran.isim + " oyuncuya " + hasar + " hasar verdi.");

        if (oyuncu.can <= 0)
        {
            oyuncu.hayatta = false;
            System.out.println("[facade] Oyuncu oldu! Oyun bitti.");
            oyundurumu.getinstance().setdevam(false);
            oyunolayyayinci.getinstance().yayinla("OYUN", "Oyun bitti - Oyuncu oldu!");
        }
    }

    public void esyatoplat(String esyaisim)
    {
        oyunnesnesi esya = nesneara(esyaisim);
        if (esya == null || !"esya".equals(esya.tip) || esya.toplandi)
        {
            System.out.println("[facade] Esya alinamaz: " + esyaisim);
            return;
        }
        esya.toplandi = true;
        esya.hayatta  = false;
        switch (esya.esyatipi)
        {
            case "can_iksiri":
                oyuncu.can = Math.min(oyuncu.can + 30, 100);
                System.out.println("[facade] Can iksiri kullanildi! Can: " + oyuncu.can);
                oyunolayyayinci.getinstance().yayinla("ESYA", "Can iksiri toplandi. Can: " + oyuncu.can);
                break;
            case "kilic":
                oyuncu.saldiri += 5;
                System.out.println("[facade] Kilic toplandi! Saldiri: " + oyuncu.saldiri);
                oyunolayyayinci.getinstance().yayinla("ESYA", "Kilic toplandi. Saldiri: " + oyuncu.saldiri);
                break;
            case "altin":
                oyundurumu.getinstance().skobekle(5);
                System.out.println("[facade] Altin toplandi! Skor: "
                        + oyundurumu.getinstance().gettoplamskor());
                oyunolayyayinci.getinstance().yayinla("ESYA",
                        "Altin toplandi. Skor: " + oyundurumu.getinstance().gettoplamskor());
                break;
        }
    }

    public void decoratorekle(String nesneisim, oyunnesnesidecorator decorator)
    {
        decoratorlar.put(nesneisim, decorator);
    }

    public void durumyazdir()
    {
        oyundurumu durum = oyundurumu.getinstance();
        System.out.println("\n--- OYUN DURUMU ---");
        System.out.println("Bolum: " + durum.getbolum() + " | Skor: " + durum.gettoplamskor());
        for (oyunnesnesi n : tumnesneler)
        {
            if (!n.hayatta) continue;
            String decbilgi = decoratorlar.containsKey(n.isim)
                    ? " [" + decoratorlar.get(n.isim).getClass().getSimpleName() + "]" : "";
            switch (n.tip)
            {
                case "oyuncu" -> System.out.println("  [OYUNCU] " + n.isim
                        + " - Can: " + n.can + " | Saldiri: " + n.saldiri
                        + " | Konum: (" + n.x + "," + n.y + ")" + decbilgi);
                case "dusman" -> System.out.println("  [DUSMAN] " + n.isim
                        + " - Can: " + n.can + " | Davranis: " + n.dusmandavranisi
                        + " | Konum: (" + n.x + "," + n.y + ")" + decbilgi);
                case "esya"   -> System.out.println("  [ESYA]   " + n.isim
                        + " - Tip: " + n.esyatipi
                        + " | Konum: (" + n.x + "," + n.y + ")");
            }
        }
        System.out.println("-------------------\n");
    }

    public List<oyunnesnesi> getTumnesneler() { return tumnesneler; }

    private oyunnesnesi ekle(oyunnesnesi n) { tumnesneler.add(n); return n; }
    private oyunnesnesi nesneara(String isim)
    {
        return tumnesneler.stream().filter(n -> n.isim.equals(isim)).findFirst().orElse(null);
    }
}

public class OyunMotoru
{
    public static void main(String[] args)
    {

        oyunolayyayinci yayinci = oyunolayyayinci.getinstance();
        konsollogdinleyici konsol  = new konsollogdinleyici();
        olumdinleyici     olumSay  = new olumdinleyici();
        bolumdinleyici    bolumDin = new bolumdinleyici();

        yayinci.dinleyiciekle(konsol);
        yayinci.dinleyiciekle(olumSay);
        yayinci.dinleyiciekle(bolumDin);

        oyunfacade facade = new oyunfacade();
        facade.baslat();
        facade.durumyazdir();

        oyunnesnesi goblinnesnesi = new dusmanfactory("saldirgan").olustur("GoblinSavas", 12, 12);
        zehirdecorator zehirligoblin = new zehirdecorator(goblinnesnesi, 5);
        facade.decoratorekle("GoblinSavas", zehirligoblin);

        oyunnesnesi oyuncuref = new oyunnesnesi("oyuncu", "Kahraman", 0, 0);
        kalkandecorator kalkanlioyuncu = new kalkandecorator(oyuncuref);
        facade.decoratorekle("Kahraman", kalkanlioyuncu);

        System.out.println("\n=== [STRATEGY:YAKIN] SALDIRI TESTLERI ===");
        facade.saldir("Goblin");
        facade.saldir("Goblin");
        facade.saldir("Goblin");
        facade.saldir("Goblin");
        facade.saldir("Goblin");

        facade.esyatoplat("Can_Iksiri");

        System.out.println("\n=== [OCP GOSTERIMI] KRITIK VURUS STRATEJISI ===");
        facade.stratejidegistir(new kritikSaldiriStratejisi());

        oyunnesnesi boss = new dusmanbuilder("BossEjderha", 20, 20)
                .can(150).saldiri(20).davranis("saldirgan").build();

        System.out.println("[TEST] Boss'a kritik vurus:");
        new kritikSaldiriStratejisi().saldir(
                new oyuncufactory().olustur("Kahraman_K", 0, 0), boss);

        System.out.println("\n=== [STRATEGY:ALAN] ALAN SALDIRISI ===");
        facade.stratejidegistir(new alanSaldiriStratejisi(facade.getTumnesneler()));
        facade.alanSaldirisi();

        System.out.println("\n=== KALKAN TESTI: Slime saldirisi ===");
        facade.dusmanSaldir("Slime");

        facade.oyuncuhareket(1, 1);
        facade.durumyazdir();

        System.out.println("=== ZEHIR DECORATOR DOGRUDAN TESTI ===");
        oyunnesnesi mantar = new dusmanfactory("pasif").olustur("ManturDusman", 3, 3);
        zehirdecorator zehirlimantar = new zehirdecorator(mantar, 8);
        oyunnesnesi fakeoyuncu = new oyunnesnesi("oyuncu", "FakeKahraman", 0, 0);
        fakeoyuncu.saldiri = 12; fakeoyuncu.can = 100;
        zehirlimantar.saldir(mantar, fakeoyuncu.saldiri);

        System.out.println("\n=== OBSERVER: BOLUM ATLAMA ===");
        oyundurumu.getinstance().setbolum(2);
        yayinci.yayinla("BOLUM", "Bolum 2'ye gecildi");

        System.out.println("\n=== OYUN SONU OZETI ===");
        System.out.println("Toplam Skor   : " + oyundurumu.getinstance().gettoplamskor());
        System.out.println("Toplam Oldurme: " + olumSay.getoldurme());
        System.out.println("Son Bolum     : " + oyundurumu.getinstance().getbolum());
    }
}