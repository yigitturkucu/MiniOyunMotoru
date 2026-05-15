import java.util.ArrayList;
import java.util.List;

public class OyunMotoru {

    String tip;
    String isim;
    int x, y;
    int can;
    int saldiri;
    int hiz;
    boolean hayatta;
    boolean toplandı;
    int skor;
    String dusmandavranisi;
    String esyatipi;

    static List<OyunMotoru> tumnesneler = new ArrayList<>();
    static OyunMotoru oyuncu;
    static int bolum = 1;
    static boolean oyundevamediyor = false;
    static int toplamskor = 0;

    public OyunMotoru(String tip, String isim, int x, int y) {
        this.tip = tip;
        this.isim = isim;
        this.x = x;
        this.y = y;
        this.hayatta = true;
        this.toplandı = false;

        if (tip.equals("oyuncu")) {
            this.can = 100;
            this.saldiri = 10;
            this.hiz = 5;
            this.skor = 0;
            oyuncu = this;
        }
        else if (tip.equals("dusman")) {
            this.can = 50;
            this.saldiri = 8;
            this.hiz = 3;
            this.dusmandavranisi = "saldirgan";
        }
        else if (tip.equals("esya")) {
            this.can = 0;
            this.saldiri = 0;
            this.hiz = 0;
            this.esyatipi = "altin";
        }

        tumnesneler.add(this);
    }

    public void hareket(int dx, int dy) {
        if (tip.equals("oyuncu")) {
            if (!hayatta) {
                System.out.println("Oyuncu öldü, hareket edemez.");
                return;
            }
            this.x += dx * hiz;
            this.y += dy * hiz;
            System.out.println(isim + " konuma gitti: (" + x + ", " + y + ")");

        }
        else if (tip.equals("dusman")) {
            if (!hayatta) return;
            if (dusmandavranisi.equals("saldirgan")) {
                if (oyuncu != null) {
                    int yonX = oyuncu.x > this.x ? 1 : -1;
                    int yonY = oyuncu.y > this.y ? 1 : -1;
                    this.x += yonX * hiz;
                    this.y += yonY * hiz;
                }
            }
            else if (dusmandavranisi.equals("pasif")) {
            }
            else if (dusmandavranisi.equals("uzaktan")) {
                if (oyuncu != null) {
                    int mesafe = Math.abs(oyuncu.x - this.x) + Math.abs(oyuncu.y - this.y);
                    if (mesafe < 10) {
                        int yonX = oyuncu.x > this.x ? -1 : 1;
                        this.x += yonX * hiz;
                    }
                }
            }
        }
        else if (tip.equals("esya")) {
        }
    }

    public void saldir(OyunMotoru hedef) {
        if (tip.equals("oyuncu")) {
            if (!this.hayatta || !hedef.hayatta) return;
            if (hedef.tip.equals("dusman")) {
                hedef.can -= this.saldiri;
                System.out.println("Oyuncu " + hedef.isim + "'a " + this.saldiri + " hasar verdi.");
                if (hedef.can <= 0) {
                    hedef.hayatta = false;
                    this.skor += 10;
                    toplamskor += 10;
                    System.out.println(hedef.isim + " öldürüldü! Skor: " + toplamskor);
                }
            }
            else if (hedef.tip.equals("oyuncu")) {
                System.out.println("Oyuncu kendine saldıramaz.");
            }
            else if (hedef.tip.equals("esya")) {
                System.out.println("Eşyaya saldırılamaz.");
            }

        }
        else if (tip.equals("dusman")) {
            if (!this.hayatta || !hedef.hayatta) return;
            if (hedef.tip.equals("oyuncu")) {
                if (dusmandavranisi.equals("saldirgan")) {
                    hedef.can -= this.saldiri;
                    System.out.println(isim + " oyuncuya " + this.saldiri + " hasar verdi.");
                }
                else if (dusmandavranisi.equals("uzaktan")) {
                    hedef.can -= (this.saldiri / 2);
                    System.out.println(isim + " oyuncuya uzaktan " + (this.saldiri / 2) + " hasar verdi.");
                }
                else if (dusmandavranisi.equals("pasif")) {
                }
                if (hedef.can <= 0) {
                    hedef.hayatta = false;
                    System.out.println("Oyuncu öldü! Oyun bitti.");
                    oyundevamediyor = false;
                }
            }

        }
        else if (tip.equals("esya")) {
            System.out.println("Eşya saldıramaz.");
        }
    }

    public void esyatopla(OyunMotoru esya) {
        if (!this.tip.equals("oyuncu")) {
            System.out.println("Sadece oyuncu eşya toplayabilir.");
            return;
        }
        if (!esya.tip.equals("esya")) {
            System.out.println("Bu bir eşya değil.");
            return;
        }
        if (esya.toplandı) {
            System.out.println("Bu eşya zaten toplandı.");
            return;
        }

        esya.toplandı = true;
        esya.hayatta = false;

        if (esya.esyatipi.equals("can_iksiri")) {
            this.can += 30;
            if (this.can > 100) this.can = 100;
            System.out.println("Can iksiri kullanıldı! Can: " + this.can);
        }
        else if (esya.esyatipi.equals("kilic")) {
            this.saldiri += 5;
            System.out.println("Kılıç toplandı! Saldırı gücü: " + this.saldiri);
        }
        else if (esya.esyatipi.equals("altin")) {
            this.skor += 5;
            toplamskor += 5;
            System.out.println("Altın toplandı! Skor: " + toplamskor);
        }
    }

    public static void oyunuguncelle() {
        if (!oyundevamediyor) return;

        for (OyunMotoru nesne : tumnesneler) {
            if (!nesne.hayatta) continue;

            if (nesne.tip.equals("dusman")) {
                nesne.hareket(0, 0);

                if (oyuncu != null && oyuncu.hayatta) {
                    int mesafe = Math.abs(oyuncu.x - nesne.x) + Math.abs(oyuncu.y - nesne.y);
                    if (mesafe <= 2) {
                        nesne.saldir(oyuncu);
                    }
                }
            }
            else if (nesne.tip.equals("oyuncu")) {
            }
            else if (nesne.tip.equals("esya")) {
            }
        }

        boolean tumdusmanlaroldu = true;
        for (OyunMotoru nesne : tumnesneler) {
            if (nesne.tip.equals("dusman") && nesne.hayatta) {
                tumdusmanlaroldu = false;
                break;
            }
        }
        if (tumdusmanlaroldu) {
            bolum++;
            System.out.println("=== BÖLÜM " + bolum + " ===");
            bolumdusmanlariniolustur();
        }
    }

    public static void bolumdusmanlariniolustur() {
        if (bolum == 2) {
            OyunMotoru d1 = new OyunMotoru("dusman", "Goblin_Lider", 20, 20);
            d1.dusmandavranisi = "saldirgan";
            d1.can = 80;
            OyunMotoru d2 = new OyunMotoru("dusman", "Ok_Atan", 30, 10);
            d2.dusmandavranisi = "uzaktan";
        }
        else if (bolum == 3) {
            OyunMotoru patron = new OyunMotoru("dusman", "PATRON", 50, 50);
            patron.dusmandavranisi = "saldirgan";
            patron.can = 200;
            patron.saldiri = 25;
        }
        else if (bolum >= 4) {
            for (int i = 0; i < bolum; i++) {
                OyunMotoru d = new OyunMotoru("dusman", "Dusman_" + i, i * 5, i * 5);
                d.can = 50 + bolum * 10;
            }
        }
    }

    public static void oyunubaslat() {
        System.out.println("Oyun başlıyor!");
        oyundevamediyor = true;

        new OyunMotoru("oyuncu", "Kahraman", 0, 0);

        OyunMotoru goblin = new OyunMotoru("dusman", "Goblin", 10, 10);
        goblin.dusmandavranisi = "saldirgan";

        OyunMotoru slime = new OyunMotoru("dusman", "Slime", 15, 5);
        slime.dusmandavranisi = "pasif";

        OyunMotoru iksir = new OyunMotoru("esya", "Can_Iksiri", 5, 5);
        iksir.esyatipi = "can_iksiri";

        OyunMotoru altin = new OyunMotoru("esya", "Altin", 8, 8);
        altin.esyatipi = "altin";
    }

    public static void durumyazdir() {
        System.out.println("--- OYUN DURUMU ---");
        System.out.println("Bölüm: " + bolum + " | Toplam Skor: " + toplamskor);
        for (OyunMotoru nesne : tumnesneler) {
            if (nesne.hayatta) {
                if (nesne.tip.equals("oyuncu")) {
                    System.out.println("[OYUNCU] " + nesne.isim + " - Can: " + nesne.can + " | Saldırı: " + nesne.saldiri + " | Konum: (" + nesne.x + "," + nesne.y + ")");
                }
                else if (nesne.tip.equals("dusman")) {
                    System.out.println("[DÜŞMAN] " + nesne.isim + " - Can: " + nesne.can + " | Davranış: " + nesne.dusmandavranisi + " | Konum: (" + nesne.x + "," + nesne.y + ")");
                }
                else if (nesne.tip.equals("esya")) {
                    System.out.println("[EŞYA] " + nesne.isim + " - Tip: " + nesne.esyatipi + " | Konum: (" + nesne.x + "," + nesne.y + ")");
                }
            }
        }
        System.out.println("-------------------");
    }

    public static void main(String[] args) {
        oyunubaslat();
        durumyazdir();

        oyunuguncelle();

        oyuncu.hareket(1, 1);

        OyunMotoru goblin = tumnesneler.get(1);
        oyuncu.saldir(goblin);
        oyuncu.saldir(goblin);
        oyuncu.saldir(goblin);
        oyuncu.saldir(goblin);
        oyuncu.saldir(goblin);

        OyunMotoru iksir = tumnesneler.get(3);
        oyuncu.esyatopla(iksir);

        oyunuguncelle();
        durumyazdir();
    }
}
