# Faz 1 AI Kullanım Logu

### AI'a ne sordunuz? (Prompt)
Projedeki "God Class" yapısını temizlemek ve nesne yaratım sorumluluğunu merkezi bir esnekliğe kavuşturmak için hangi Creational örüntüleri kullanabileceğimi sordum. Ayrıca kod bloklarının formatlanması ve Git terminal komutları hakkında yardım istedim.

### AI ne yanıtladı? (Özet)
AI; nesne yaratımındaki karmaşık if-else bloklarını merkezi bir soyutlamaya taşımak adına Factory Method ve Builder pattern'lerini, global oyun durumunu tek elden yönetmek için ise Singleton pattern'ini birlikte kullanabileceğimi söyledi. Kod incelemesinde (review) ise nesne yaratımının düzeltildiğini ancak gelecekte hareket/saldırı metotlarının da polymorphism ile alt sınıflara dağıtılması gerektiğini belirtti.

### Siz ne uyguladınız ve neden farklı/aynı?
AI'ın önerdiği Singleton, Factory Method ve Builder örüntülerinin mantığını projeye bire bir uyguladım. Ayrıca kodun okunabilirliğini arttırmak ve kendi kodlama alışkanlıklarıma uydurmak için yapısal değişiklikler yaptım.