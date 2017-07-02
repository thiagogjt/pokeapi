# pokeapi

## Desenvolvimento


Antes que você possa construir este projeto, você deve instalar e configurar os seguintes dependências em sua máquina:

1. [Node.js][]: Node é usado para executar um servidor web desenvolvimento e construir o projeto.
                    Dependendo do seu sistema, você pode instalar o Node a partir do fonte ou como um pacote pré-embalados. 

Depois de instalar o Node, você deve ser capaz de executar o seguinte comando para instalar as ferramentas de desenvolvimento.
Você só precisará executar este comando quando dependências mudarem no [package.json] (package.json).

    npm install

O [Gulp] [] é usado como nosso sistema de compilação. Gulp instalar a ferramenta de linha de comando do Gulp globalmente globalmente com:

    npm install -g gulp-cli

Execute os seguintes comandos em dois terminais separados para criar uma experiência de desenvolvimento bem-aventurada, onde seu navegador
recarrega a página quando os arquivos são alterados no seu disco rígido.

    ./mvnw
    gulp

[Bower] [] é usado para gerenciar dependências de CSS e JavaScript utilizados nesta aplicação. Você pode atualizar as dependências 
especificando uma versão mais recente em [bower.json](bower.json). Você também pode executar `bower update` e `bower install` para gerenciar as dependências. 
Adicionar o flag `-h` em qualquer comando para ver como você pode usá-lo. Por exemplo, , `bower update -h`.

## Construindo para a produção

Para otimizar a aplicação pokeapi para a produção, execute

    ./mvnw -Pprod clean package

Isto irá concatenar e "minificar" os arquivos CSS e JavaScript. Ele também irá modificar `index.html` para que ele referencie os novos arquivos.
Para garantir que tudo funcionou, execute:

    java -jar target/*.war

Então acesse o endereço [http://localhost:8080](http://localhost:8080) no seu navegador.

## Testando

Para executar os testes da aplicação execute:

    ./mvnw clean test

### Teste do Cliente

Testes no cliente são executados pelo [Karma][] e escritos com o [Jasmine][].
Eles estão localizados em [src/test/javascript/](src/test/javascript/) e podem ser executados com o comando: 

    gulp test

[Node.js]: https://nodejs.org/
[Yarn]: https://yarnpkg.org/
[Bower]: http://bower.io/
[Gulp]: http://gulpjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
[Leaflet]: http://leafletjs.com/
[DefinitelyTyped]: http://definitelytyped.org/
