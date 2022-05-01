# ReaxEssentials
Плагин, добавляющий реализацию самых обычных команд 

Команды 
------ 
- **/sethome** - Установить точку дома. Подразумевается что название точки - ваш ник
  > Количество точек дома настраивается правом `PERM: reaxessentials.home.amount.<amount>`
  - **/sethome** `<homeName>` - Установить точку дома  


- **/delhome** - Удалить точку дома. Подразумевается что название точки - ваш ник
  - **/delhome** `<homeName>` - Удалить точку дома


- **/home** - Телепортироваться на точку дома. Подразумевается что название точки - ваш ник

  - **/home list** - Посмотреть список доступных точек дома
- **/home** `<homeName>` - Телепортироваться на свою точку дома с данным именем 
  
  - **/home** `<playerName>` `<homeName>` - Телепортироваться на точку дома игрока с данным именем


- **/homeinvite** `<playerName>` - Пригласить игрока на точку дома. Подразумевается что название точки - ваш ник

  - **/homeinvite** `<playerName>` `<homeName>` - Пригласить игрока на данную точку дома 
- **/homeuninvite** `<playerName>` - Забрать приглашение у игрока на точку дома. Подразумевается что название точки - ваш ник

  - **/homeuninvite** `<playerName>` `<homeName>` - Забрать приглашение у игрока на данную точку дома


- **/back** - Вернуться на точку смерти

  - **/back** `<playerName>` - Вернуть на точку смерти игрока (`PERM: reaxessentials.back.others`)


- **/feed** - Восполнить голод (`PERM: reaxessentials.feed`)

  - **/feed** `<playerName>` - Восполнить голод игроку (`PERM: reaxessentials.feed.others`) 


- **/heal** - Восполнить голод и здоровье (`PERM: reaxessentials.heal`)

  - **/heal** `<playerName>` - Восполнить голод и здоровье для игрока (`PERM: reaxessentials.heal.others`)


- **/fly** - Включить/Выключить режим полёта (`PERM: reaxessentials.fly`)

  - **/fly** `<playerName>` - Включить/Выключить режим полёта для игрока (`PERM: reaxessentials.fly.others`)  


- **/god** - Включить/Выключить режим бога (`PERM: reaxessentials.god`)

  - **/god** `<playerName>` - Включить/Выключить режим бога для игрока (`PERM: reaxessentials.god.others`)


- **/playtime** - Узнать своё наигранное время

  - **/playtime** `<playerName>` - Узнать наигранное время игрока (`PERM: reaxessentials.playtime.others`) 


- **/tpr** | **/rtp** | **/tprandom** - Телепортироваться на рандомную точку на карте

  - **/tpr** `<playerName>` - Телепортировать игрока на рандомную точку (`PERM: reaxessentials.tpr.others`) 


- **/teleport** | **/tp** | **/tppos** - Телепортироваться к точке/игроку (`PERM: reaxessentials.teleport`)

  - **/tp** `<playerName>` - Телепортироваться к игроку (`PERM: reaxessentials.teleport`)

    - **/tp** `<playerName>` `<playerName>` - Телепортировать игрока к игроку (`PERM: reaxessentials.teleport.others`)
  - **/tp** `<position>` - Телепортироваться на точку на карте (`PERM: reaxessentials.teleport`) 
  
    - **/tp** `<position>` `<playerName>` - Телепортировать игрока на точку (`PERM: reaxessentials.teleport.others`)


- **/timeset** `<time>` - Изменить игровое время (`PERM: reaxessentials.timeset`)

  - **/day** - Включить день

  - **/night** - Включить ночь


- **/weather** `<sun | strom | thunder>` - Изменить игровую погоду (`PERM: reaxessentials.weather`)

  - **/sun** - Включить солнечную погоду

  - **/storm** | **/rain** - Включить дождь 

  - **/thunder** - Включить дождь с грозой


- **/adminchat** | **/a** | **/ac** `<message>` - Отправить сообщение в админ-чат (`PERM: reaxessentials.adminchat`)  


- **/broadcast** | **/bc** | **/announce** `<message>` - Сделать объявление всему серверу (`PERM: reaxessentials.broadcast`) 


- **/say** `<message>` - Написать в чат по-особому (`PERM: reaxessentials.say`)


- **/personalmessage** | **/m** `<playerName>` `<message>` - Отправить личное сообщение игроку


- **/reply** | **/r** `<message>` - Ответить на последнее личное сообщение своим сообшением


- **/ignore** `<playerName>` - Игнорировать личные сообщения от игрока
- **/unignore** `<playerName>` - Перестать игнорировать личные сообщения от игрока


- **/helpop** `<message>` - Попросить помощи у администрации
  
  - **/helpop reply** `<taskId>` `<message>` - Ответить на вопрос игрока (`PERM: reaxessentials.helpop.answer`)
  - **/helpop reserve** `<taskId>` - Забронировать за собой этот вопрос (`PERM: reaxessentials.helpop.answer`)
  - **/helpop info** `<taskId>` - Посмотреть ин-фу по данному вопросу (`PERM: reaxessentials.helpop.answer`)
  - **/helpop list**  - Посмотреть все ожидающие запросы (`PERM: reaxessentials.helpop.answer`)
  - **/helpop mark** `<taskId>` `<mark>` `[comment]` - Поставить оценочку на ответ от администрации


- **/balance** | **/bal** - Посмотреть свой текущий баланс

  - **/bal** `<playerName>` - Посмотреть баланс у игрока (`PERM: reaxessentials.balance.others`)


- **/pay** `<playerName>` `<amount>` - Передать игроку монетки


- **/economy** | **/eco** `<give | take | set>` `<playerName>` `<amount>` - Дать/Забрать/Установить игроку на баланс некую сумму (`PERM: reaxessentials.economy`)

  - **/eco take** `<playerName>` `<amount>` - Забрать монетки
  - **/eco give** `<playerName>` `<amount>` - Дать монеток
  - **/eco set** `<playerName>` `<amount>` - Установить количество монеток на какое-то значение


- **/kit** | **/kit list**  - Посмотреть список доступных наборов

  - **/kit** `<kitName>` - Получить набор (`PERM: reaxessentials.kit.receive.<kitName>`)
- **/kitdel** `<kitName>` - Удалить набор (`PERM: reaxessentials.kit.change`)
- **/kitcreate** `<kitName>` - Создать набор (`PERM: reaxessentials.kit.change`) 
- **/kit edit** `<kitName>` `<kitName>` `<content | delay | name>` `[newValue]` - Изменить набор (`PERM: reaxessentials.kit.change`)

  - **/kit edit** `<kitName>` **content** - Изменить предметы в ките (`PERM: reaxessentials.kit.change`)
  - **/kit edit** `<kitName>` **delay** `<time>` - Изменить время, после которого кит можно получить повторно (`PERM: reaxessentials.kit.change`)
  - **/kit edit** `<kitName>` **name** `<kitName>` - Изменить название кита (`PERM: reaxessentials.kit.change`)


- **/gamemode** `<0 | 1 | 2 | 3>` | `<survival | creative | adventure | spectator>`  - Изменить режим игры (`PERM: reaxessentials.gamemode`)

  - **/gmc** - Включить режим креатив
    
      - **/gmc** `<playerName>` - Включить режим креатив для игрока (`PERM: reaxessentials.gamemode.others`)
  - **/gms** - Включить режим выживания

    - **/gms** `<playerName>` - Включить режим выживания для игрока (`PERM: reaxessentials.gamemode.others`)
  - **/gma** - Включить режим приключения

    - **/gma** `<playerName>` - Включить режим приключения для игрока (`PERM: reaxessentials.gamemode.others`)
  - **/gmsp** - Включить режим наблюдения

    - **/gmsp** `<playerName>` - Включить режим наблюдения для игрока (`PERM: reaxessentials.gamemode.others`) 

- **/ban** `<playerName>` `<time>` `<reason>` - Забанить игрока (`PERM: reaxessentials.ban`)
- **/unban** `<playerName>` - Разбанить игрока (`PERM: reaxessentials.unban`)


- **/mute** `<playerName>` `<time>` `<reason>` - Выдать мут игроку (`PERM: reaxessentials.mute`)
- **/unmute** `<playerName>` - Размутить игрока (`PERM: reaxessentials.unmute`)


- **/warn** `<playerName>` `<reason>` - Выдать варн игроку (`PERM: reaxessentials.warn`)
- **/warnclear** `<playerName>` `[amount]` - Забрать у игрока варны. Все или указанное число (`PERM: reaxessentials.warn.clear`)


- **/kick** `<playerName>` `<reason>` - Кикнуть игрока (`PERM: reaxessentials.kick`)


- **/warns** - Посмотреть кол-во своих варнов
  
  - **/warns** `<playerName>` - Посмотреть кол-во варнов у игрока (`PERM: reaxessentials.warnsamount.others`)


- **/setwarp** `<warpName>` - Установить точку варпа (`PERM: reaxessentials.setwarp`) 
  > Количество варпов настраивается правом `PERM: reaxessentials.warp.amount.<amount>`
- **/delwarp** `<warpName>` - Удалить точку варпа (`PERM: reaxessentials.delwarp`)
- **/warp** `<warpName>` - Переместиться на точку варпа


- **/delhome** - Удалить точку дома. Подразумевается что название точки - ваш ник
  - **/delhome** `<homeName>` - Удалить точку дома


- **/home** - Телепортироваться на точку дома. Подразумевается что название точки - ваш ник

  - **/home list** - Посмотреть список доступных точек дома
- **/home** `<homeName>` - Телепортироваться на свою точку дома с данным именем

  - **/home** `<playerName>` `<homeName>` - Телепортироваться на точку дома игрока с данным именем


- **/setspawn** - Установить точку спавна (`PERM: reaxessentials.setspawn`)
- **/spawn** - Телепортироваться на точку спавна


- **/call** | **/tpa** `<playerName>` - Отправить запрос на телепортацию к игроку
- **/tpdeny** `<playerName>` - Отказать в телепортации
- **/tpaccept** `<playerName>` - Принять запрос телепортации


- **/tpignore** - Запретить/Разрешить все запросы на телепортацию к себе
   
  - **/tpignore** `<playerName>` - Запретить/Разрешить все запросы на телепортацию к игроку (`PERM: reaxessentials.tpignore.others`)


- **/reaxessentials** - Основная команда

  - **/reaxessentials reload** - Перезагрузить конфиги плагина (`PERM: reaxessentials.*`)