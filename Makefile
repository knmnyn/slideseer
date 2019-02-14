##################################################
# $Id: Makefile,v 1.1 2006/05/24 02:18:19 min Exp min $ / Min-Yen Kan
# 
#	RCS: $Id: Makefile,v 1.1 2006/05/24 02:18:19 min Exp min $
# 
#	RCS: $Log: Makefile,v $
#	RCS: Revision 1.1  2006/05/24 02:18:19  min
#	RCS: Initial revision
#	RCS:
##################################################

##############################
# MACROS
##############################

SLIDESEERDIR 	=	slideseer
CGIDIR	=	doc/
VERSION = 	1.1

TAR	=	/bin/gtar
RM	=	/bin/rm
CP	=	/bin/cp
CHMOD	=	/bin/chmod
EXEC    =	bin/ssSpider

##############################
# TARGETS
##############################

all:

clean:
	rm -f core *~ bin/*~ *.tgz doc/tmp/* $(CGIDIR)/core

reallyclean:	clean
	rm -f bin/* bin/util/*

exec:	$(EXEC)
	$(CHMOD) a+x $(CGI) $(EXEC)

tgz:	exec README.html CHANGELOG.html
	$(RM) -f slideseer-all$(VERSION).tgz slideseer$(VERSION).tgz
	cd ..; \
	mv $(SLIDESEERDIR) $(SLIDESEERDIR)$(VERSION); \
	$(TAR) -cpzvf $(SLIDESEERDIR)$(VERSION)/slideseer$(VERSION).tgz --exclude=slideseer$(VERSION).tgz --exclude=RCS --exclude=eval $(SLIDESEERDIR)$(VERSION);
	cd ..; mv $(SLIDESEERDIR)$(VERSION) $(SLIDESEERDIR);

# all omits the RCS directory as well as large ppt HTML files.

alltgz:	exec README.html CHANGELOG.html
	$(RM) -f slideseer-all$(VERSION).tgz slideseer$(VERSION).tgz
	cd ..; \
	mv $(SLIDESEERDIR) $(SLIDESEERDIR)$(VERSION); \
	$(TAR) -cpzvf $(SLIDESEERDIR)$(VERSION)/slideseer-all$(VERSION).tgz \
           --exclude=slideseer-all$(VERSION).tgz $(SLIDESEERDIR)$(VERSION);
	cd ..; mv $(SLIDESEERDIR)$(VERSION) $(SLIDESEERDIR);

## below are testing targets for distribution integrity


