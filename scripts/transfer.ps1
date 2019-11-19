$data = @{
    sourceSystemId = 100;
    sourceAccountId = 1001;
    targetAccountId = 1002;
    amount = 1.5
};

$json = ConvertTo-Json -InputObject $data
$url = 'http://localhost:8090/transfer'

Invoke-WebRequest -Uri $url -Method Post -Body $json