# copy file backup vào mongodb container folder
docker cp ./management_system_utilities/database 1cf22f744516:/data/backup

# chạy câu lệnh để restore database
docker exec -it 1cf22f744516 mongorestore -d management_system /data/backup/management_system
