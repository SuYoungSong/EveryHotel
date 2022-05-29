create table 숙박업체(
	업체아이디 varchar(20) not null ,
    업체명 varchar(20) not null,
    연락처 varchar(20) default '010-0000-0000',
    사업자번호 char(12) not null,
    비밀번호 varchar(20) not null,
    주소 varchar(30) not null,
    primary key (업체아이디)
);
create table 숙소(
	숙소번호 int not null auto_increment,
    가격 int not null,
    분류 varchar(10) not null,
    호실 varchar(10) not null,
    숙소정원 int not null,
    업체아이디 varchar(20) not null,
    foreign key(업체아이디) references 숙박업체(업체아이디) on delete cascade,
    primary key(숙소번호)
);
create table 이미지(
	이미지번호 int not null auto_increment,
    숙소번호 int not null,
    이미지주소 varchar(50) not null,
    foreign key (숙소번호) references 숙소(숙소번호) on delete cascade,
    primary key(이미지번호)
);
create table 회원(
	회원아이디 varchar(20) not null,
    비밀번호 varchar(20) not null,
    이름 varchar(10) default '익명',
    연락처 varchar(20) default '010-0000-0000',
    생년월일 date not null,
    primary key (회원아이디)
);

create table 예약(
	예약번호 int auto_increment not null,
    예약일 date not null,
    퇴실일 date not null,
    숙소번호 int,
    회원아이디 varchar(20) not null,
    foreign key (숙소번호) references 숙소(숙소번호) on update set null,
    foreign key (회원아이디) references 회원(회원아이디) on delete cascade,
    primary key(예약번호)
);


create table 리뷰(
    리뷰번호 int auto_increment not null,
    리뷰내용 varchar(100) default ' ',
    평점 int not null,
    작성일자 datetime default now(),
    숙소번호 int not null,
    예약번호 int not null,
    foreign key(예약번호) references 예약(예약번호) on delete cascade,
    foreign key(숙소번호) references 숙소(숙소번호) on delete cascade,
    primary key(리뷰번호)
);
