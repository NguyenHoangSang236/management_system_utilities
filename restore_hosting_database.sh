# copy file backup vào mongodb container folder
docker cp ./management_system_utilities/database 1cf22f744516:/data/db

# chạy câu lệnh để restore database
docker exec -it mongodb-container mongorestore -d management_system /data/db/database/management_system

