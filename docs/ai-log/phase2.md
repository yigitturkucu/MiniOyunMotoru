# Faz 2 AI Kullanım Logu ve Kritik Analiz

### AI'a ne sordunuz? (Prompt)
1. Oyun projemdeki alt sistemlerin (hareket, savaş, esya toplama) karmaşıklığını gizlemek için "Adapter" pattern burada uygun mu yoksa "Facade" mi kullanmalıyım?
2. Oyun nesnelerine çalışma zamanında (runtime) dinamik olarak yeni özellikler (zehirli saldırı, kalkan koruması) eklemek için "Decorator" örüntüsünü nasıl entegre edebilirim?

### AI ne yanıtladı? (Özet)
* **Facade için:** AI; Adapter'ın uyumsuz arayüzleri dönüştürdüğünü, Facade'ın ise karmaşık sistemi basitleştirdiğini belirterek projem için Facade'ın doğru tercih olduğunu söyledi ve bir taslak sundu.
* **Decorator için:** `oyunnesnesi` sınıfını sarmalayacak bir `oyunnesnesidecorator` soyut sınıfı ve bundan türeyen `zehirdecorator`, `kalkandecorator` somut sınıflarını oluşturmamı önerdi.

### AI'ın Yanlış/Eksik Önerisi (Kritik Tespit)
* **Facade Örüntüsündeki Eksiklik:** AI, Facade sınıfını önerirken bu yapının zamanla tüm iş mantığını kendi içine çekerek bir **God Class (Anti-Pattern)** haline dönüşebileceği uyarısını yapmadı. Facade sadece bir yönlendirici (delegator) olmalıdır. Projede uyguladığım `oyunfacade` sınıfının işi kendi içinde if-else bloklarıyla çözmek yerine, sorumlulukları alt sınıflara devretmesini (delegation) sağlayarak bu mimari hatanın önüne geçtim.
* **Decorator Örüntüsündeki Eksiklik:** AI, Decorator yapısını önerirken nesne hiyerarşisindeki sarmalama derinliği arttıkça nesne kimliği sorunları yaşanabileceğini belirtmedi. Yani, bir nesne üst üste zehir ve kalkanla sarmalandığında orijinal `oyunnesnesi` referansına erişmek zorlaşıyordu. Bu eksikliği fark ederek decorator sınıfımın içine orijinal nesneyi döndüren bir `getnesne()` metodu ekledim ve mimari bağımlılık krizini çözdüm.