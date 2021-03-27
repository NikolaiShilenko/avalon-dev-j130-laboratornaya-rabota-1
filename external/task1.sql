create table authors (
    id int primary key, 
    name varchar(64) not null,
    text varchar(255)
);

create table documents (
    id int primary key, 
    name varchar(64) not null,
    text varchar(1024),
    create_date date default current_date,
    author int not null references authors (id)
);

insert into authors
    values (1, 'Arnold Grey', null),
    (2, 'Tom Hawkins', 'new author'),
    (3, 'Jim Beam', null);

insert into documents (id, name, text, author)
    values (1, 'Project plan', 'First content', 1),
    (2, 'First report', 'Second content', 2),
    (3, 'Test result', 'Third content', 2),
    (4, 'Second report', 'Report content', 3);
    
update authors set text = 'No data' where text is null;

select name from documents where author = (select id from authors where name = 'Tom Hawkins');

select id, name, author from documents where name like '%report%';

/*

Задача №1.

Исходные данные.
База данных test содержит две таблицы. Таблица Authors имеет следующую
структуру:
 идентификатор автора, представленный целым числом, является
первичным ключом таблицы;
 поле с именем и фамилией автора длиной до 64 символов включительно,
которое не может быть пустым;
 поле примечания длиной до 255 символов включительно.
Данная таблица содержит данные:
1, 'Arnold Grey', -
2, 'Tom Hawkins', 'new author'
3, 'Jim Beam', -

Таблица Documents имеет следующую структуру:
 идентификатор документа, представленный целым числом, является
первичным ключом таблицы;
 поле названия документа длиной до 64 символов включительно, которое не
может быть пустым;
 поле с основным текстом документа длиной до 1024 символов
включительно;
 поле даты создания документа, которое должно быть обязательно
заполнено;
 поле ссылки на автора документа, которое является внешним ключом,
ссылающимся на первичный ключ таблицы Authors, и которое также не
может быть пустым.
Данная таблица содержит данные:
1, 'Project plan', 'First content', 1
2, 'First report', 'Second content', 2
3, 'Test result', 'Third content', 2
4, 'Second report', 'Report content', 3

Для данных таблиц.
1. Напишите последовательность операторов SQL (SQL-скрипт), которая
создаёт указанные таблицы и заполняет их вышеуказанными данными.
2. Напишите оператор UPDATE, который в таблице авторов все пустые (null)
комментарии заменяет на строку 'No data'.
3. Напишите оператор SELECT, который запрашивает названия всех
документов, автором которых является Tom Hawkins.
4. Напишите оператор SELECT, который запрашивает идентификатор,
название документа и его автора, при условии, что в названии документа
есть слово report.

*/