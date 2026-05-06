$lines = Get-Content "E:\code\mylearn\testruoyi\.qoder\skills\backend-engineer\STANDARDS.md"; 398..402 | ForEach-Object { Write-Host ("{0}: {1}" -f $_, $lines[$_-1].Replace("`t", "[TAB]")) }
