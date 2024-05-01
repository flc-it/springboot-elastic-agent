# Delete and stop the service if it already exists.
if (Get-Service heartbeat -ErrorAction SilentlyContinue) {
  $service = Get-WmiObject -Class Win32_Service -Filter "name='heartbeat'"
  $service.StopService()
  Start-Sleep -s 1
  $service.delete()
}

$workdir = Split-Path $MyInvocation.MyCommand.Path

# Create the new service.
New-Service -name heartbeat `
  -displayName "Elastic Heartbeat" `
  -description "Monitor services for their availability with active probing" `
  -binaryPathName "`"$workdir\heartbeat.exe`" --environment=windows_service -c `"$workdir\heartbeat.yml`" --path.home `"$workdir`" --path.data `"$env:PROGRAMDATA\elastic\heartbeat`" --path.logs `"$env:PROGRAMDATA\elastic\heartbeat\logs`" -E logging.files.redirect_stderr=true"
  -dependsOn = "elasticsearch-service-x64"

# Attempt to set the service to demanded start using sc config.
Try {
  Start-Process -FilePath sc.exe -ArgumentList 'config heartbeat start= demand'
}
Catch { Write-Host -f red "An error occured setting the service to demand." }
