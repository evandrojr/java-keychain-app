# Script PowerShell para instalar dependências do Windows para o Java Keychain App
# Salve como install-deps.ps1 e inclua no instalador

# Verifica se o PowerShell é recente (>=5)
$psVersion = $PSVersionTable.PSVersion.Major
if ($psVersion -lt 5) {
    Write-Host "PowerShell 5 ou superior é necessário. Você pode instalar pela Microsoft Store (recomendado) ou pelo site oficial."
    Write-Host "Abrindo a Microsoft Store..."
    Start-Process "ms-windows-store://pdp/?productid=9MZ1SNWT0N5D" -Wait
    Write-Host "Se preferir, baixe pelo site oficial: https://github.com/PowerShell/PowerShell"
    exit 1
}

# Instala o módulo CredentialManager se não estiver presente
if (-not (Get-Module -ListAvailable -Name CredentialManager)) {
    Write-Host "Instalando módulo CredentialManager..."
    try {
        Install-Module -Name CredentialManager -Scope CurrentUser -Force -AllowClobber -ErrorAction Stop
        Write-Host "CredentialManager instalado com sucesso."
    } catch {
        Write-Host "Erro ao instalar o módulo CredentialManager. Tente instalar manualmente:"
        Write-Host "Install-Module -Name CredentialManager -Scope CurrentUser"
    }
} else {
    Write-Host "CredentialManager já está instalado."
}

# (Opcional) Verifica se o Java está instalado
if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Host "Java não encontrado no PATH. Instale o Java 7+ para rodar o aplicativo."
    Start-Process "https://adoptium.net/pt/" -Wait
}
