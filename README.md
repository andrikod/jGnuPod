jGnuPod
===============

jGnuPod is a graphical interface written in Java Swing for the GNUpod command line tool.


Background
-----
Around 2007 I got an iPod Nano 2th Generation as a present. As a linux user, I found out that they were a plenty of tools for managing an iPod (gtkpod, amarok) for linux. However after some years, it got very frustrating: saving music to iPod was causing constant random crashes, or the iPod managers were focusing on features like podcasts or smartlists, which I wasn't/still am not using.

I just wanted to drag-and-drop some folders into iPod and play. That's it..

GNUpod was exactly what I needed, but unfortunatelly there was no GUI. So, I decided to implement one.


Supported Operations
-----
Although GNUpod provides a lot of operations, jGnuPod supports only the ones, which I really wanted:
* Mount iPod
* Display saved files
* Add new music
* Delete music

That's it..

Install/Run
-----
The project is an Eclipse project. 
To generate the jar, use the build.xml and run Ant Build.

Run the jar:
```
java -jar jGnupod.jar
```


Required Installed Packages
-----
* GNUpod tools
For Ubuntu: `sudo apt-get install gnupod-tools`


Perl Incompatibility
-----
Due to an incompatibility issue, GNUpod tools don't work properly with perl5.
The following error occurs:

```
defined(@array) is deprecated at /usr/share/perl5/GNUpod/XMLhelper.pm line 362.
        (Maybe you should just omit the defined()?)
```

After some research I found out that the problem is known (https://rt.cpan.org/Public/Bug/Display.html?id=79658),
and the reason is the deprecated defined method. I solved the problem by modifying directly the file /usr/share/perl5/GNUpod/XMLhelper.pm
replacing:

```
    if (defined(@{$XDAT->{playlists}->{data}->{$current_plname}})) { #the playlist is not empty
```

to:

```
   if (@{$XDAT->{playlists}->{data}->{$current_plname}}) { #the playlist is not empty
```


Disclaimer
-----
The purpose of this project was only a quick fix for my every day problem. The code maybe be ugly/old/dirty, it should have been a maven project and not an Eclipse, but I no longer use the iPod and I have no intention to keep working on it. Mostly a backup.



Links
-----
* GnuPod Homepage -- https://www.gnu.org/software/gnupod/
