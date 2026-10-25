@echo off
rem Forward FTC Dashboard ports from the Control Hub over USB, then open http://localhost:8080/dash
rem 8080 serves the dashboard page, 8000 is its websocket (needed for op mode controls)
rem Drop any stale Wi-Fi connection to the hub so adb uses USB
adb disconnect 192.168.43.1:5555
adb forward tcp:8080 tcp:8080
adb forward tcp:8000 tcp:8000
