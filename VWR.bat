@echo off
CD C:\V2AutoWR
ant deleteoldreportsandlogs createmasterxlsx createtestngxml createtestcases clean compile run makexsltreports emailreports createdashboard 
PAUSE