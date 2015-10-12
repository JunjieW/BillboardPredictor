import urllib
import re
import HTMLParser

# We need to write a input including the specific urls during 
# a certina period, then iterate to get the html

url = 'http://www.umdmusic.com/default.asp?Lang=English&Chart=D'
sock = urllib.urlopen(url)
li = sock.read()
sock.close()

class BoardTableParser(HTMLParser.HTMLParser, ):
    def __init__(self):
        HTMLParser.HTMLParser.__init__(self)
        self.in_td = False
        self.in_tr = False
        self.start_print = False
        
    def handle_comment(self, data):
        if data.find("Display Chart Table") > 0:
            self.start_print = True
            #print "============= find comment =================="
    
    def handle_starttag(self, tag, attrs):
        if tag == 'td':
            self.in_td = True
        if tag == 'tr':
			self.in_tr = True
    
    def handle_data(self, data):
        if self.start_print == True:
            if self.in_td:
                pattern = re.compile('\s+') 
                str_data = re.sub(pattern,' ',data)
                print str_data+',',
    
    def handle_endtag(self, tag):
        if tag == 'td':
            self.in_td = False
        if tag == 'tr':
            print ''
            self.in_tr = False


            
p = BoardTableParser()
p.feed(li)

# TODO: wirte data into text file rather than in console