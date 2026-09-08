#!/bin/sh
# Render cap cho container 1 cong qua bien PORT (mac dinh 8080 neu chay local/Docker thuong).
# Tomcat mac dinh nghe port 8080 va co 1 "shutdown port" rieng (8005) de nhan lenh tat server.
# Tren Render, cong shutdown do gay nham lan (bi he thong health-check bat nham).
# Script nay: tat han shutdown port, va cho Tomcat nghe dung PORT Render yeu cau.

PORT="${PORT:-8080}"

sed -i "s/port=\"8080\"/port=\"${PORT}\"/" /usr/local/tomcat/conf/server.xml
sed -i 's/port="8005"/port="-1"/' /usr/local/tomcat/conf/server.xml

exec catalina.sh run
