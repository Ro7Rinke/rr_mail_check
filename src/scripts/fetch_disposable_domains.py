import requests
import csv

# URLs das listas públicas de e-mails descartáveis
urls = [
    "https://raw.githubusercontent.com/disposable/disposable-email-domains/master/domains.txt",
    "https://raw.githubusercontent.com/amieiro/disposable-email-domains/master/denyDomains.txt"
]

all_domains = set()  # Usar set para evitar duplicatas

for url in urls:
    print(f"Fetching {url} ...")
    try:
        response = requests.get(url)
        response.raise_for_status()
        lines = response.text.splitlines()
        # Remover comentários e espaços
        lines = [line.strip() for line in lines if line.strip() and not line.startswith("#")]
        all_domains.update(lines)
    except Exception as e:
        print(f"Erro ao baixar {url}: {e}")

# Salvar em CSV pronto para importar no banco
output_file = "disposable_domains.csv"
with open(output_file, "w", newline="") as csvfile:
    writer = csv.writer(csvfile)
    writer.writerow(["domain"])  # Cabeçalho
    for domain in sorted(all_domains):
        writer.writerow([domain])

print(f"Salvo {len(all_domains)} domínios em {output_file}")