$data = @{
    sourceSystemId = 100;
    accountId = 1001;
};

$json = ConvertTo-Json -InputObject $data
$url = 'http://localhost:8090/get'

Invoke-WebRequest -Uri $url -Method Post -Body $json