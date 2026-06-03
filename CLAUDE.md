# CLAUDE.md

Bu dosya, Claude Code'un bu projede nasıl çalışacağını tanımlar.

## Wiki - Kalıcı Bilgi Tabanı

Bu proje, Andrej Karpathy'nin LLM Wiki metodolojisine dayanan **Claude Wiki** kullanır.
Wiki, `docs/wiki/` ve `docs/raw/` dizinleri altında bulunur.

### Mimari

Üç katman:
1. **docs/raw/** — Değiştirilemez kaynaklar. Sadece OKU, ASLA değiştirme.
2. **docs/wiki/** — Senin oluşturduğun ve yönettiğin sayfalar.
3. **CLAUDE.md** (bu dosya) — Wiki davranışını tanımlayan şema.

Local-only mod: Obsidian entegrasyonu yapılandırılmadı.
Obsidian senkronizasyonu için: `claude mcp add obsidian -- npx @bitbonsai/mcpvault@latest /path/to/your/vault`

### Sayfa Formatı

Her wiki sayfası şu YAML frontmatter'a sahip olmalıdır:

    ---
    title: "Sayfa Adı"
    type: source | entity | concept | decision | learning | query
    created: YYYY-MM-DD
    updated: YYYY-MM-DD
    tags: [etiket1, etiket2]
    sources: [ilgili kaynak dosyalar]
    related: [ilgili wiki sayfaları]
    ---

Çapraz referanslar için wikilink `[[sayfa-adi]]` kullan.
Dosya adları kebab-case: `benim-konseptim.md`.
Sayfaları kullanıcının konuştuğu dilde yaz.

### Oturum Başlangıç Protokolü

Her oturumun BAŞINDA, başka bir şey yapmadan ÖNCE:
1. `docs/wiki/index.md` dosyasını oku
2. `docs/wiki/log.md` dosyasını oku
3. `docs/raw/` içinde henüz işlenmemiş yeni dosyaları kontrol et

### Operasyonlar

#### INGEST — Kaynak işleme

Kullanıcı `docs/raw/` içindeki bir kaynağı işlemek istediğinde:

1. Kaynağın tam içeriğini oku
2. Kullanıcıyla ana noktaları tartış
3. `docs/wiki/sources/<kaynak-adi>.md` sayfası oluştur
4. Her teknoloji, kütüphane, servis için `docs/wiki/entities/` altında sayfa oluştur veya GÜNCELLE
5. Her pattern, mimari, metodoloji için `docs/wiki/concepts/` altında sayfa oluştur veya GÜNCELLE
6. `docs/wiki/index.md` dosyasını güncelle
7. `docs/wiki/log.md` dosyasına kayıt ekle
8. Sonucu raporla: oluşturulan sayfalar, güncellenen sayfalar, çıkarılan entity'ler

#### QUERY — Wiki'de arama

Kullanıcı soru sorduğunda:
1. `docs/wiki/index.md` oku
2. İlgili sayfaları oku ve sentezle
3. Yanıt değerliyse, `docs/wiki/queries/` altına kaydetmeyi öner

#### LINT — Sağlık kontrolü

- Sayfalar arası çelişkileri kontrol et
- Yetim sayfaları bul (gelen bağlantı yok)
- Kendi sayfası olmayan kavramları bul
- Eksik çapraz referansları kontrol et
- Güncelliğini yitirmiş bilgileri tespit et
- Yeni kaynaklar veya araştırmalar öner
- Lint sonucunu logla

#### DECISION — Karar kaydetme

Önemli bir karar alındığında `docs/wiki/decisions/<karar-adi>.md` sayfası oluştur.

#### LEARNING — Öğrenilen ders kaydetme

Bir hata düzeltildiğinde, içgörü ortaya çıktığında `docs/wiki/learnings/<ogrenme-adi>.md` oluştur.

#### SYNC — Senkronizasyon (Obsidian için, şu an pasif)

### Otomatik Davranışlar

- **Hata düzeltildi mi?** Learning olarak kaydetmeyi öner
- **Mimari karar mı?** Decision olarak kaydetmeyi öner
- **Değerli analitik yanıt mı?** Query olarak arşivlemeyi öner
- **Mevcut wiki bilgisiyle çelişki mi?** Kullanıcıyı uyar
- **docs/raw/ içinde yeni dosya mı?** Ingest etmeyi öner

### Hızlı Komutlar

- `wiki ingest <dosya>` — Belirli kaynağı işle
- `wiki ingest all` — docs/raw/ içindeki tüm yeni kaynakları işle
- `wiki query <soru>` — Wiki'de sorgu yap
- `wiki lint` — Sağlık kontrolü yap
- `wiki status` — İstatistikleri göster (sayfalar, kaynaklar, son güncelleme)
- `wiki decision <başlık>` — Karar kaydet
- `wiki learning <başlık>` — Öğrenilen dersi kaydet
- `wiki search <terim>` — Wiki sayfalarında ara
- `wiki sync` — Lokal → Obsidian senkronizasyonu zorla
